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
            Header()
            Content().frame(
                minWidth: 0,
                maxWidth: .infinity,
                minHeight: 0,
                maxHeight: .infinity,
                alignment: .bottomTrailing
              )
            Footer()
        }
    }
}

struct Header : View{
    
    @State var selected = 0
    
    var body: some View{
        HStack{
            Button {
                
            } label: {
                Image(systemName: "house").resizable().frame(width: 18, height: 18, alignment: .center).padding()
            }.buttonStyle(PlainButtonStyle())

            Button {
                selected = 0
            } label: {
                Text("Time").font(.title3).foregroundColor(Color.white).padding()
            }.buttonStyle(PlainButtonStyle()).if(selected == 0) { View in
                View.background(Color.white.opacity(0.2))
            }.if(selected != 0) { View in
                View.background(Color.clear)
            }
            
            Button {
                selected = 1
            } label: {
                Text("Projects").font(.title3).foregroundColor(Color.white).padding()
            }.buttonStyle(PlainButtonStyle()).if(selected == 1) { View in
                View.background(Color.white.opacity(0.2))
            }.if(selected != 1) { View in
                View.background(Color.clear)
            }
            
            Button {
                selected = 2
            } label: {
                Text("Reports").font(.title3).foregroundColor(Color.white).padding()
            }.buttonStyle(PlainButtonStyle()).if(selected == 2) { View in
                View.background(Color.white.opacity(0.2))
            }.if(selected != 2) { View in
                View.background(Color.clear)
            }

            
        }.frame(minWidth: 0, maxWidth: .infinity,alignment: .topLeading)
            .background(Color("headerColor"))

    }
}

struct Content : View{
    var body: some View{
        VStack{
            
        }
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
