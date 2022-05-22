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
            
                MainContent(date:$date,newEntry: {
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
            }.frame(minWidth: 800 , minHeight: 500, alignment: .top)
            
            
            NewTimeEntryOverlay(date:$date,createNewEntry:$createNewEntry)
            
        }
                  

    }
}

struct NewTimeEntryOverlay : View{
    @Binding var date:Date
    @Binding var createNewEntry:Bool

    var body: some View{
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
