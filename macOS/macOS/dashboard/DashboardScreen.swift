//
//  DashboardScreen.swift
//  macOS
//
//  Created by Anmol Verma on 11/05/22.
//

import Foundation
import SwiftUI

struct DashboardScreen : View{
    var body: some View{
        VStack{
            DashHeader()
        
            Content().frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .topLeading
            ).background(.white)
            Footer()
        }            .frame(width: 800 , height: 600, alignment: .top)

    }
}


struct Content : View{
    @State var date:Date = Date.now

    var body: some View{
        VStack{
            DayNavigateButtons(date: date) { Date in
                self.date = Date
            }
            
            HStack{
                NewEntryButton().padding(12)
                VStack{
                    WeekView( date:$date)
                    DayRecord()
                }
            }
        }
       
        
    }
}

struct DayRecord: View{
    var body: some View{
        HStack{
            
        }
    }
}

struct NewEntryButton: View{
    var body: some View{
        VStack{
            HStack{
                
                Image(systemName: "plus")
                    .resizable()
                    .foregroundColor(Color.white)
                    .frame(width: 30, height: 30, alignment: .center)
                
            }.padding().onTapGesture(perform: {
                
            })
            .background(Color("greenColor")).cornerRadius(8)
            
            Text("New Entry").padding(4)
        }
    }
}

struct WeekView: View{
    @Binding var date:Date
    
    var body: some View{
        ScrollView (.horizontal, showsIndicators: false) {
            HStack {
                ForEach (getCurrentWeek(date: date), id: \.self) { loopDate in
                    CalendarDay (date: loopDate,selectedDate:$date)
                }
        }
    }
}
}

struct CalendarDay : View{
    
    @State var date:Date
    @Binding var selectedDate : Date
    
    var body: some View{
        VStack{
            Text(date.dayOfWeek()).padding(Edge.Set.bottom, 8).if(date.dayOfWeek() == selectedDate.dayOfWeek()) { View in
                View.font(Font.title3.weight(.bold))
            }
            Text("0.0").if(date.dayOfWeek() == selectedDate.dayOfWeek()) { View in
                View.font(Font.title3.weight(.bold))
            }
        }.padding()
    }
}

struct Footer : View{
    var body: some View{
        HStack{
            
        }
    }
}


extension View {
    /// Applies the given transform if the given condition evaluates to `true`.
    /// - Parameters:
    ///   - condition: The condition to evaluate.
    ///   - transform: The transform to apply to the source `View`.
    /// - Returns: Either the original `View` or the modified `View` if the condition is `true`.
    @ViewBuilder func `if`<Content: View>(_ condition: Bool, transform: (Self) -> Content) -> some View {
        if condition {
            transform(self)
        } else {
            self
        }
    }
}
