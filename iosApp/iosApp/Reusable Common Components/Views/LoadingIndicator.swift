//
//  LoadingIndicator.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 25/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct LoadingIndicator: ViewModifier {
    
    let show: Bool
    let title: String
    let size: Int
    let color: Color
    
    func body(content: Content) -> some View {
        if show {
            ZStack {
                content
                    .overlay(Color.gray.opacity(0.4))
                ProgressView(title)
                    .tint(.black)
                    .foregroundColor(.black)
                    .scaleEffect(2)
                    .font(.caption)
            }
        } else {
            content
        }
    }
}

extension View {
    func loadingIndicator(show: Bool,
                          title: String = "",
                          size: Int = 2,
                          color: Color = .black) -> some View {
        modifier(LoadingIndicator(show: show, title: title, size: size, color: color))
    }
}
