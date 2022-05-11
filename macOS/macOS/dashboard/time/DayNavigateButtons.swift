//
//  DayNavigateButtons.swift
//  macOS
//
//  Created by Anmol Verma on 11/05/22.
//

import Foundation
import SwiftUI

struct DayNavigateButtons : View{
    @State var date:Date = Date.now

    var body: some View{
        HStack{
            NavigateButtons {
                self.date = Calendar.current.date(byAdding: .day, value: -1, to: date)!
            } onRight: {
                self.date = Calendar.current.date(byAdding: .day, value: 1, to: date)!
            }

            PresentDay(date:$date)
            Spacer()
            CalendarIcon()

            DayWeekSwitcher {
                
            } onRight: {
                
            }

        }
    }
}

struct DayWeekSwitcher : View{
    var onLeft: (() -> Void)
    var onRight: (() -> Void)
    @State var selected = 0

    var body: some View{
        HStack{

            Text("Day").padding().if(selected == 0) { View in
                View.background(Color("appColor").opacity(0.2))
            }.if(selected != 0) { View in
                View.background(Color.clear)
            }.onTapGesture {
                onLeft()
                selected = 0
            }
     
            
            Divider().frame(maxHeight:40)
            
            Text("Week").padding().onTapGesture(perform: {
                onRight()
                selected = 1
            }).if(selected == 1) { View in
                View.background(Color("appColor").opacity(0.2))
            }.if(selected != 1) { View in
                View.background(Color.clear)
            }
           
            
        }.overlay(
            RoundedRectangle(cornerRadius: 25)
                .stroke(.gray.opacity(0.4), lineWidth: 1)
        ).padding()
    }
}

struct CalendarIcon : View{
    var body: some View{
        HStack{
            Button {
            } label: {
                Image(systemName:"calendar")
            }.buttonStyle(PlainButtonStyle()).padding(12)
        }.overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.4), lineWidth: 1)
        ).padding()
        
    }
}

struct PresentDay : View{
    @Binding var date:Date

    
    var body: some View{
        VStack{
            Text(formattedDate())
        }
    }
    
    func formattedDate() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.doesRelativeDateFormatting = true
        let uploadDate = dateFormatter.string(from:date)
        return uploadDate
    }
}


struct NavigateButtons : View{
    var onLeft: (() -> Void)
    var onRight: (() -> Void)

    var body: some View{
        HStack{
            Button {
                onLeft()
            } label: {
                Image(systemName:"arrow.left")
            }.buttonStyle(PlainButtonStyle()).padding()
            
            Divider().frame(maxHeight:30)
            Button {
                onRight()
            } label: {
                Image(systemName:"arrow.right")
            }.buttonStyle(PlainButtonStyle()).padding()
            
        }.overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(.gray.opacity(0.4), lineWidth: 1)
        ).padding()
    }
}
