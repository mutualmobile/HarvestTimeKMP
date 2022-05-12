//
//  WeekView.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI

struct WeekView: View{
    @Binding var date:Date
    var dateUpdate: ((Date)->Void)

    var body: some View{
        HStack {
            ForEach (getCurrentWeek(date: date), id: \.self) { loopDate in
                CalendarDay(date: loopDate,selectedDate:$date) { date in
                    dateUpdate(date)
                }
            }
        }
    }
}


struct CalendarDay : View {
    
    @State var date:Date
    @Binding var selectedDate : Date
    var dateUpdate: ((Date)->Void)

    var body: some View{
        Button {
            selectedDate = date
            dateUpdate(selectedDate)
        } label: {
            VStack{
                Text(date.dayOfWeek()).padding(Edge.Set.bottom, 8).if(date.dayOfWeek() == selectedDate.dayOfWeek()) { View in
                    View.font(Font.title3.weight(.bold))
                }
                Text("0.0").if(date.dayOfWeek() == selectedDate.dayOfWeek()) { View in
                    View.font(Font.title3.weight(.bold))
                }
                Rectangle().fill(date.dayOfWeek() == selectedDate.dayOfWeek() ? Color("appColor") : Color.clear)
                                                        .frame(height: 3)
            }
        }.buttonStyle(PlainButtonStyle())

    }
}
