//
//  TimeSheetView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct TimeSheetView_Previews: PreviewProvider {
    static var previews: some View {
        TimeSheetView()
    }
}


class TimeSheetStore: ObservableObject {
    var logHour: WeekLogHour

    init() {
        self.logHour = WeekLogHour(saturday: 0.0,
                                   sunday: 0.0,
                                   monday: 3.0,
                                   tuesday: 8.00,
                                   wednesday: 8.0,
                                   thursday: 4.30,
                                   friday: 1.2)
    }
    
    func validate(for selectedDay: WeekLogHour.Day) {
        objectWillChange.send()
        
        if selectedDay.isToday {
            self.logHour.today = selectedDay
            self.logHour.selectedDay = nil
        } else {
            self.logHour.selectedDay = selectedDay
            self.logHour.today = nil
        }
    }
}

struct TimeSheetView: View {
    
    @ObservedObject var store = TimeSheetStore()
    
    @State private var calenderTapped = false
    @State private var infoTapped = false
    @State private var moreTapped = false
    @State private var presentTimeEntrySheet = false
    
    @DateHandler private var headerDate = Date()
    
    var body: some View {
        VStack(alignment: .center) {
            headerView
            Spacer()
            ContainerView(presentTimeEntrySheet: $presentTimeEntrySheet, quoteText: "")
            Spacer()
        }
    }
    
    private var headerView: some View {
        VStack {
            HStack {
                Text($headerDate)
                    .padding(.leading)
                Spacer()
                Button {
                    calenderTapped = true
                } label: {
                    Image(systemName: "calendar")
                }
                .buttonStyle(BorderlessButtonStyle())
                .padding(.trailing)
                
                Button {
                    infoTapped = true
                } label: {
                    Image(systemName: "info.circle")
                }
                .buttonStyle(BorderlessButtonStyle())
                .padding(.trailing)
                
                Button {
                    moreTapped = true
                } label: {
                    Image(systemName: "ellipsis")
                }
                .buttonStyle(BorderlessButtonStyle())
                .padding(.trailing)
                
            }
            WeekView(logHours: store.logHour) { selectedDay in
                store.validate(for: selectedDay)
            }
        }
        .padding(.vertical)
        .background(.quaternary)
    }
}

struct ContainerView: View {
    
    @Binding var presentTimeEntrySheet: Bool
    var quoteText: String
    
    var body: some View {
        VStack() {
            Text(quoteText)
                .multilineTextAlignment(.center)
            Button {
                presentTimeEntrySheet = true
            } label: {
                Text("Add New Entry")
                    .padding()
                    .overlay(
                        RoundedRectangle(cornerRadius: 15)
                            .stroke(ColorAssets.colorBackground.color, lineWidth: 1)
                    )
            }
        }
    }
}
