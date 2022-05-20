//
//  ColorAssets.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 20/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

enum ColorAssets: String {
    case colorBackground
    case white
    
    var color: Color {
        Color(self.rawValue)
    }
    
    var uiColor: UIColor {
        UIColor(color)
    }
}
