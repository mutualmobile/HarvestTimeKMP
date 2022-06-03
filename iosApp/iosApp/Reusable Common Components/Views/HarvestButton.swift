//
//  HarvestButton.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 25/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct HarvestButton: ViewModifier {
    func body(content: Content) -> some View {
        content
            .foregroundColor(.black)
            .font(.title3)
            .frame(width: UIScreen.main.bounds.width - 70, height: 45)
            .background(ColorAssets.white.color)
            .cornerRadius(10.0)
    }
}

extension View {
    func harvestButton() -> some View {
        modifier(HarvestButton())
    }
}
