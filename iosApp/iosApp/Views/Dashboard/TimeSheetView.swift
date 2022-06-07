//
//  TimeSheetView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

class TimeSheetStore: ObservableObject {
    @Published var selectedDay: Day?
    
    var days: [Day]

    init() {
        self.days = TimeSheetStore.getDays()
    }
    
    
    // TODO: As of now it act as network service
    static func getDays() -> [Day] {
        return [
            .saturday(0.0, false, false),
            .sunday(0.0, false, false),
            .monday(8.0, false, false),
            .tuesday(8.556, false, false),
            .wednesday(9.87, false, false),
            .thursday(0.0, false, false),
            .friday(0.0, false, false)
        ]
    }
    
    func validate(for day: Day?) {
        if var day = day {
            
            let index = day.index
            
            var days = TimeSheetStore.getDays()
            
            days.remove(at: index)
            
            if case .tuesday = day {
                day.setMatched(true)
            } else {
                day.setSelected(true)
            }
            
            days.insert(day, at: index)

            self.days = days
            selectedDay = day
        }
    }
}

struct TimeSheetView: View {
    
    @ObservedObject var store = TimeSheetStore()
    
    var selectedDay: Binding<Day?> {
        Binding {
            store.selectedDay
        } set: { value in
            store.validate(for: value)
        }

    }
    
    var body: some View {
        WeekView(seletedDay: selectedDay, days: store.days)
    }
}
