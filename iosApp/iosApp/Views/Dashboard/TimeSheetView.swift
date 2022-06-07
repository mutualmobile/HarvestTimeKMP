//
//  TimeSheetView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct TimeSheetView: View {
    
    private let days: [Day] = [.saturday(0.0), .sunday(0.0), .monday(8.0), .tuesday(8.556), .wednesday(9.87), .thursday(0.0), .friday(0.0)]
    
    var body: some View {
        WeekView(days)
    }
}
