//
//  CalendarHelper.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation


func getCurrentWeek(date:Date) -> [Date] {
    var calendar = Calendar.autoupdatingCurrent
    calendar.firstWeekday = 2 // Start on Monday (or 1 for Sunday)
    let today = calendar.startOfDay(for: date)
    var week = [Date]()
    if let weekInterval = calendar.dateInterval(of: .weekOfYear, for: today) {
        for i in 0...6 {
            if let day = calendar.date(byAdding: .day, value: i, to: weekInterval.start) {
                week += [day]
            }
        }
    }
    return week
}

extension Date {
    func dayOfWeek() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        return dateFormatter.string(from: self).capitalized
        // or use capitalized(with: locale) if you want
    }
    
    func dayNumberOfWeek() -> Int? {
           return Calendar.current.dateComponents([.weekday], from: self).weekday
       }
}
