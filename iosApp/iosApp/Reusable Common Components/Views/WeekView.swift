//
//  WeekView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI


struct WeekView_Previews: PreviewProvider {
    static var days: [Day] = [
        .saturday(0.0, false, false),
        .sunday(0.0, false, false),
        .monday(8.0, false, false),
        .tuesday(8.556, false, false),
        .wednesday(9.87, false, false),
        .thursday(0.0, false, false),
        .friday(0.0, false, false)
    ]
    static var previews: some View {
        WeekView(seletedDay: .constant(.monday(0.0, false, false)), days: days)
    }
}

struct WeekView: View {
    
    @Environment(\.colorScheme) var colorScheme
    
    let days: [Day]
    var seletedDay: Binding<Day?>
    init(seletedDay: Binding<Day?>, days: [Day]) {
        self.seletedDay = seletedDay
        self.days = days
    }
    
    private let horizontalPadding: CGFloat = 8
    private let verticalPadding: CGFloat = 4
    
 
    
    var body: some View {
        List {
            HStack(alignment: .center) {
                ForEach(days) { day in
                    Button {
                        if self.seletedDay.wrappedValue != day {
                            self.seletedDay.wrappedValue = day
                        }
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
    }
    
    private func circleFilledColor(for day: Day) -> Color? {
        if day.isSelectedMatched.matchedDay {
            return ColorAssets.colorBackground.color
        } else if day.isSelectedMatched.selectedDay {
            return colorScheme == .dark ? ColorAssets.white.color : ColorAssets.black.color
        }
        return nil
    }
    
    private func cicleTextColor(for day: Day) -> Color? {
        if day.isSelectedMatched.matchedDay || day.isSelectedMatched.selectedDay {
            return colorScheme == .dark ? ColorAssets.black.color : ColorAssets.white.color
        }
        return nil
    }
    
    private func hourTextColor(for day: Day) -> Color {
        if day.isSelectedMatched.matchedDay {
            return ColorAssets.colorBackground.color
        }
        return colorScheme == .dark ? ColorAssets.white.color : ColorAssets.black.color
    }
}
