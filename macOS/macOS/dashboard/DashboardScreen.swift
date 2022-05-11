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
              )
            Footer()
        }            .frame(width: 800 , height: 600, alignment: .top)

    }
}


struct Content : View{
    var body: some View{
        VStack{
            DayNavigateButtons()
            HStack{
                NewEntryButton()
                VStack{
                    WeekView()
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
        HStack{
            
        }
    }
}

struct WeekView: View{
    var body: some View{
        HStack{
            
            Image(systemName: "plus").resizable().frame(width: 40, height: 40, alignment: .center)
            
        }.overlay(
            RoundedRectangle(cornerRadius: 25).background(Color("greenColor"))
        ).padding().onTapGesture(perform: {
            
        })
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
