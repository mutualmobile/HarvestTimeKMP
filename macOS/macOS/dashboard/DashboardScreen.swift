//
//  DashboardScreen.swift
//  macOS
//
//  Created by Anmol Verma on 11/05/22.
//

import Foundation
import SwiftUI

struct DashboardScreen : View{
    
    @State private var createNewEntry = false
    @State var date:Date = Date.now

    
    var body: some View{
       
        ZStack{
            VStack{
                DashHeader()
            
                Content(date:$date,newEntry: {
                    createNewEntry = true
                },dateUpdate:{ Date in
                    date = Date
                }).frame(
                    minWidth: 0,
                    maxWidth: .infinity,
                    minHeight: 0,
                    maxHeight: .infinity,
                    alignment: .topLeading
                ).background(.white)
                Footer()
            }.frame(minWidth: 800 , minHeight: 600, alignment: .top)
            
            if createNewEntry == true {
                Color.black.opacity(0.4).edgesIgnoringSafeArea(.all).onTapGesture {
                    createNewEntry = false
                }
                NewTimeEntryModal(date: $date) {
                    createNewEntry = false
                }
            }
        }
                  

    }
}


struct Content : View{
    @Binding var date:Date
    var newEntry: (()->Void)
    var dateUpdate: ((Date)->Void)

    var body: some View{
        VStack{
            DayNavigateButtons(date: date) { Date in
                self.date = Date
                dateUpdate(Date)
            }
            
            HStack{
                
                NewEntryButton(newEntry: newEntry).padding(12)
                VStack{
                    WeekView(date:$date)
                    DayRecord(date:$date)
                }
            }
        }
       
        
    }
}

struct DayRecord: View{
    @Binding var date:Date

    var body: some View{
        HStack {
            
        }
    }
}

struct NewEntryButton: View{

    var newEntry: (()->Void)
    
    var body: some View{
        VStack{
            HStack{
                
                Image(systemName: "plus")
                    .resizable()
                    .foregroundColor(Color.white)
                    .frame(width: 30, height: 30, alignment: .center)
                
            }.padding().onTapGesture(perform: {
                newEntry()
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
