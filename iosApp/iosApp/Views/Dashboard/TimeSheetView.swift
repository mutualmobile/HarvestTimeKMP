//
//  TimeSheetView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

class TimeSheetStore: ObservableObject {
    var logHour: WeekLogHour

    init() {
        self.logHour = WeekLogHour(saturday: 0.0,
                                   sunday: 0.0,
                                   monday: 3.0,
                                   tuesday: 8.00,
                                   wednesday: 8.0,
                                   thursday: 4.30,
                                   friday: 1.2)
    }
    
    func validate(for day: WeekLogHour.Day) {
        objectWillChange.send()
        if case .wednesday(_) = day {
            self.logHour.today = day
            self.logHour.selectedDay = nil
        } else {
            self.logHour.selectedDay = day
            self.logHour.today = nil
        }
        

    }
}

struct TimeSheetView: View {
    
    @ObservedObject var store = TimeSheetStore()
    
    var body: some View {
        WeekView(logHours: store.logHour) { selectedDay in
            store.validate(for: selectedDay)
        }
    }
}
