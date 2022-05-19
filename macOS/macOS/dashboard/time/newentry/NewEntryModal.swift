//
//  NewEntryModal.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI

struct NewTimeEntryModal : View{
    @Binding var date:Date
    @State var projectDesc:String=""
    @State var projectTime:String=""

    var newEntry: (()->Void)

    var body: some View {
        VStack(alignment:.leading){
            
            Text("New Time Entry for \(date.formattedAs(format:"EEEE, dd MMM"))")
                .frame(
                      minWidth: 400,
                      maxWidth: .infinity,
                      alignment: .topLeading
                    )
                .padding(8)
                .background(.gray.opacity(0.2))
            
            Text("Project / Task").bold().padding(4)
            
            Projects().frame(width:400).padding()
            Projects().frame(width:400).padding()

            
            HStack{
                TextField(text: $projectDesc, label: {
                    Text("Notes (Optional)").font(.title)
                }).padding()
                
                TextField(text: $projectTime, label: {
                    Text("0.00").font(.title)
                }).padding()
            }
            
            HStack{
                
                Button {
                    newEntry()
                } label: {
                    Text("Save Time").padding(4)
                        .background(Color("greenColor"))
                        .foregroundColor(Color.white)
                        .cornerRadius(6)
                }.buttonStyle(PlainButtonStyle())

                Button {
                    newEntry()
                } label: {
                    Text("Cancel").padding(4)
                        .background(Color.gray.opacity(0.5))
                        .foregroundColor(Color.white)
                        .cornerRadius(6)
                }.buttonStyle(PlainButtonStyle())
            }.padding()
        }.frame(width: 450, alignment: .center)
            
            .background(.white)
            .cornerRadius(4)
            
    }
}




extension Date{
    func formattedAs(format:String) -> String{
        let formatter = DateFormatter()
        formatter.dateFormat = format
        return formatter.string(from: self)
    }
}
