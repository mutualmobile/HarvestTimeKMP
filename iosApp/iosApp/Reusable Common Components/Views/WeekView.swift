//
//  WeekView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct WeekView_Previews: PreviewProvider {
    
    static var logHour = WeekLogHour(saturday: 0.0,
                                     sunday: 0.0,
                                     monday: 3.0,
                                     tuesday: 8.00,
                                     wednesday: 8.0,
                                     thursday: 4.30,
                                     friday: 1.2,
                                     selectedDay: .friday(1.2),
                                     today: .wednesday(8))
    

    static var previews: some View {
        WeekView(logHours: logHour) { selectedDay in
            _ = selectedDay
        }
    }
}

struct WeekView: View {
    
    @Environment(\.colorScheme) var colorScheme
    
    let logHours: WeekLogHour
    var daySelection: (WeekLogHour.Day) -> Void

    init(logHours: WeekLogHour, daySelection: @escaping (WeekLogHour.Day) -> Void) {
        self.daySelection = daySelection
        self.logHours = logHours
    }
    
    private let horizontalPadding: CGFloat = 8
    private let verticalPadding: CGFloat = 4
    
    var body: some View {
        HStack(alignment: .center) {
            ForEach(logHours.days) { day in
                Button {
                    daySelection(day)
                } label: {
                    VStack {
                        Text(day.tuple.name)
                            .padding(EdgeInsets(top: verticalPadding,
                                                leading: horizontalPadding,
                                                bottom: verticalPadding,
                                                trailing: horizontalPadding))
                            .background(circleFilledColor(for: day))
                            .clipShape(Circle())
                            .foregroundColor(cicleTextColor(for: day))
                        
                        Text(day.tuple.hours)
                            .foregroundColor(hourTextColor(for: day))
                    }
                    .frame(maxWidth: .infinity)
                }
                .buttonStyle(BorderlessButtonStyle())
            }
        }
        .font(.caption2)
    }
    
    private func circleFilledColor(for day: WeekLogHour.Day) -> Color? {
        
        if let today = logHours.today, today == day {
            return ColorAssets.colorBackground.color
        } else if let selectedDay = logHours.selectedDay, selectedDay == day {
            return colorScheme == .dark ? ColorAssets.white.color : ColorAssets.black.color
        }
        return nil
    }
    
    private func cicleTextColor(for day: WeekLogHour.Day) -> Color? {
        
        if let today = logHours.today, today == day {
            return colorScheme == .dark ? ColorAssets.white.color : ColorAssets.black.color
        } else if let selectedDay = logHours.selectedDay, selectedDay == day {
            return colorScheme == .dark ? ColorAssets.black.color : ColorAssets.white.color
        }
        return nil
    }
    
    private func hourTextColor(for day: WeekLogHour.Day) -> Color {
        if let today = logHours.today, today == day {
            return ColorAssets.colorBackground.color
        }
        return colorScheme == .dark ? ColorAssets.white.color : ColorAssets.black.color
    }
}

class WeekLogHour {
    var days: [Day]
    var selectedDay: Day?
    var today: Day?
    
    init(saturday: Double,
         sunday: Double,
         monday: Double,
         tuesday: Double,
         wednesday: Double,
         thursday: Double,
         friday: Double,
         selectedDay: Day? = nil,
         today: Day? = nil) {
        
        self.days = [
            .saturday(saturday),
            .sunday(sunday),
            .monday(monday),
            .tuesday(tuesday),
            .wednesday(wednesday),
            .thursday(thursday),
            .friday(friday)
        ]
        self.selectedDay = selectedDay
        self.today = today
    }
}

extension WeekLogHour {
    enum Day: Identifiable, Equatable {
        
        var id: UUID { UUID() }
        
        case saturday(Double)
        case sunday(Double)
        case monday(Double)
        case tuesday(Double)
        case wednesday(Double)
        case thursday(Double)
        case friday(Double)
        
        var tuple: (name: String, hours: String) {
            switch self {
            case .saturday(let hours):
                return ("S" , String(format: "%.2f", hours))
            case .sunday(let hours):
                return ("S" , String(format: "%.2f", hours))
            case .monday(let hours):
                return ("M" , String(format: "%.2f", hours))
            case .tuesday(let hours):
                return ("T" , String(format: "%.2f", hours))
            case .wednesday(let hours):
                return ("W" , String(format: "%.2f", hours))
            case .thursday(let hours):
                return ("T" , String(format: "%.2f", hours))
            case .friday(let hours):
                return ("F" , String(format: "%.2f", hours))
            }
        }
        
        var isToday: Bool {
            guard let weekDay = Calendar.current.dateComponents([.weekday], from: Date()).weekday else {
                return false
            }

            switch weekDay {
            case 1:
                return self == .sunday(hours)
            case 2:
                return self == .monday(hours)
            case 3:
                return self == .tuesday(hours)
            case 4:
                return self == .wednesday(hours)
            case 5:
                return self == .thursday(hours)
            case 6:
                return self == .friday(hours)
            default:
                return self == .saturday(hours)
            }
        }
        
        // TODO: Can be made public in future.
        private var hours: Double {
            switch self {
            case .saturday(let hours):
                return hours
            case .sunday(let hours):
                return hours
            case .monday(let hours):
                return hours
            case .tuesday(let hours):
                return hours
            case .wednesday(let hours):
                return hours
            case .thursday(let hours):
                return hours
            case .friday(let hours):
                return hours
            }
        }
    }
}
