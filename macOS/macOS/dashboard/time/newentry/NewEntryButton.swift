//
//  NewEntryButton.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI

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
