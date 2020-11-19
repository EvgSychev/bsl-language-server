/*
 * This file is a part of BSL Language Server.
 *
 * Copyright © 2018-2020
 * Alexey Sosnoviy <labotamy@gmail.com>, Nikita Gryzlov <nixel2007@gmail.com> and contributors
 *
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * BSL Language Server is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * BSL Language Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with BSL Language Server.
 */
package com.github._1c_syntax.bsl.languageserver.providers;

import com.github._1c_syntax.bsl.languageserver.context.DocumentContext;
import com.github._1c_syntax.bsl.languageserver.context.callee.CalleeStorage;
import com.github._1c_syntax.bsl.languageserver.context.symbol.MethodSymbol;
import com.github._1c_syntax.bsl.languageserver.utils.MdoRefBuilder;
import com.github._1c_syntax.bsl.languageserver.utils.Ranges;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.lsp4j.CallHierarchyIncomingCall;
import org.eclipse.lsp4j.CallHierarchyIncomingCallsParams;
import org.eclipse.lsp4j.CallHierarchyItem;
import org.eclipse.lsp4j.CallHierarchyOutgoingCall;
import org.eclipse.lsp4j.CallHierarchyOutgoingCallsParams;
import org.eclipse.lsp4j.CallHierarchyPrepareParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.SymbolTag;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toCollection;

@Component
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class CallHierarchyProvider {

  private final CalleeStorage calleeStorage;

  public List<CallHierarchyItem> prepareCallHierarchy(
    DocumentContext documentContext,
    CallHierarchyPrepareParams params
  ) {
    Position position = params.getPosition();

    // TODO: SymbolResolver.getSymbol
    return documentContext.getSymbolTree()
      .getMethods().stream()
      .filter(methodSymbol -> Ranges.containsPosition(methodSymbol.getSubNameRange(), position))
      .findAny()
      .or(() -> calleeStorage.getCalledMethodSymbol(documentContext.getUri(), position)
        .map(Pair::getKey)
      )
      .map(CallHierarchyProvider::getCallHierarchyItem)
      .map(Collections::singletonList)
      // в случае отсутствия ответа по протоколу надо возвращать null, а не пустой список
      .orElse(Collections.emptyList())
      ;
  }

  public List<CallHierarchyIncomingCall> incomingCalls(
    DocumentContext documentContext,
    CallHierarchyIncomingCallsParams params
  ) {

    var serverContext = documentContext.getServerContext();
    CallHierarchyItem item = params.getItem();

    return documentContext.getSymbolTree().getMethodSymbol(item.getName())
      .stream()
      .flatMap(methodSymbol ->
        calleeStorage.getCalleesOf(
          MdoRefBuilder.getMdoRef(documentContext),
          documentContext.getModuleType(),
          methodSymbol
        ).stream()
      )
      .map((Location location) -> {
        DocumentContext document = serverContext.getDocument(location.getUri());
        return document.getSymbolTree().getMethodSymbol(location.getRange())
          .map(CallHierarchyProvider::getCallHierarchyItem)
          .map(callHierarchyItem -> Pair.of(callHierarchyItem, location.getRange()));
      })
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(groupingBy(Pair::getKey, mapping(Pair::getValue, toCollection(ArrayList::new))))
      .entrySet()
      .stream()
      .map(entry -> new CallHierarchyIncomingCall(entry.getKey(), entry.getValue()))
      .collect(Collectors.toList());
  }

  public List<CallHierarchyOutgoingCall> outgoingCalls(
    DocumentContext documentContext,
    CallHierarchyOutgoingCallsParams params
  ) {

    return calleeStorage.getCalledMethodSymbolsFrom(documentContext.getUri(), params.getItem().getRange())
      .entrySet().stream()
      .map((Map.Entry<MethodSymbol, Collection<Range>> entry) -> {
        var callHierarchyItem = getCallHierarchyItem(entry.getKey());
        Collection<Range> value = entry.getValue();
        return new CallHierarchyOutgoingCall(callHierarchyItem, new ArrayList<>(value));
      })
      .collect(Collectors.toList());

  }

  @SuppressWarnings("UnstableApiUsage")
  private static CallHierarchyItem getCallHierarchyItem(MethodSymbol methodSymbol) {
    CallHierarchyItem item = new CallHierarchyItem();
    item.setUri(methodSymbol.getUri().toString());
    item.setName(methodSymbol.getName());
    item.setKind(methodSymbol.getSymbolKind());
    item.setRange(methodSymbol.getRange());
    item.setSelectionRange(methodSymbol.getSubNameRange());

    List<SymbolTag> tags = methodSymbol.isDeprecated()
      ? Collections.singletonList(SymbolTag.Deprecated)
      : Collections.emptyList();
    item.setTags(tags);

    return item;
  }
}
