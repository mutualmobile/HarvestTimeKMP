//
//  WeekView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct WeekView: View {
    
    let days: [Day]
    
    init(_ days: [Day]) {
        self.days = days
    }
    
    private let horizontalPadding: CGFloat = 6
    private let verticalPadding: CGFloat = 3
    
    var body: some View {
        List {
            HStack(alignment: .center) {
                ForEach(days) { day in
                    VStack {
                        Text(day.logHours.0)
                            .padding(EdgeInsets(top: verticalPadding,
                                                leading: horizontalPadding,
                                                bottom: verticalPadding,
                                                trailing: horizontalPadding))
                            .border(Color.red)
                        Text(day.logHours.1)
                        
                    }
                    .frame(maxWidth: .infinity)
                }
            }
            .font(.caption2)
        }
    }
}
