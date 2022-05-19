//
//  DashHeader.swift
//  macOS
//
//  Created by Anmol Verma on 11/05/22.
//

import Foundation
import SwiftUI

struct DashHeader : View{
    
    @State var selected = 0
    
    var body: some View{
        HStack{
            Button {
                
            } label: {
                Image(systemName: "house").resizable().foregroundColor(.white).frame(width: 18, height: 18, alignment: .center).padding()
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
