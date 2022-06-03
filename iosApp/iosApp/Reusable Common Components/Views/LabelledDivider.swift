//
//  LabelledDivider.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 25/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

// From: https://stackoverflow.com/questions/56619043/show-line-separator-view-in-swiftui
struct LabelledDivider: View {
    
    let label: String
    let horizontalPadding: CGFloat
    let color: Color
    
    init(label: String, horizontalPadding: CGFloat = 10, color: Color = .black) {
        self.label = label
        self.horizontalPadding = horizontalPadding
        self.color = color
    }
    
    var body: some View {
        HStack {
            line
            Text(label)
                .foregroundColor(color)
            line
        }.padding(.horizontal)
    }
    
    var line: some View {
        VStack {
            Divider()
                .background(color)
        }
        .padding(horizontalPadding)
    }
}
