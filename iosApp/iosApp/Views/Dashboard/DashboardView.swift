//
//  DashboardView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI


struct DashboardView_Previews: PreviewProvider {
    static var previews: some View {
        DashboardView()
    }
}

struct DashboardView: View {
    
    private enum Tabs: Int {
        case timeSheet
        case reportSheet
        case account
    }
    
    @State private var selectedTab: Tabs = .timeSheet
    
    var body: some View {
        
        TabView(selection: $selectedTab) {
            TimeSheetView()
                .tag(Tabs.timeSheet)
                .tabItem { tabItem(for: .timeSheet) }
            
            ReportSheetView()
                .tag(Tabs.reportSheet)
                .tabItem { tabItem(for: .reportSheet) }
            
            AccountView()
                .tag(Tabs.account)
                .tabItem { tabItem(for: .account) }
        }
        .accentColor(ColorAssets.colorBackground.color)
    }
    
    private func tabItem(for tab: Tabs) -> some View {
        switch tab {
        case .timeSheet:
            return Label("Time", systemImage: "timer")
        case .reportSheet:
            return Label("Report", systemImage: "chart.line.uptrend.xyaxis")
        case .account:
            return Label("Account", systemImage: "person.circle.fill")
        }
    }
}

enum Day: Identifiable {
    var id: UUID { UUID() }
    
    case saturday(Double)
    case sunday(Double)
    case monday(Double)
    case tuesday(Double)
    case wednesday(Double)
    case thursday(Double)
    case friday(Double)
    
    var logHours: (String, String) {
        switch self {
        case .saturday(let hours):
            return ("S" ,  String(format: "%.2f", hours))
        case .sunday(let hours):
            return ("S" ,  String(format: "%.2f", hours))
        case .monday(let hours):
            return ("M" ,  String(format: "%.2f", hours))
        case .tuesday(let hours):
            return ("T" ,  String(format: "%.2f", hours))
        case .wednesday(let hours):
            return ("W" ,  String(format: "%.2f", hours))
        case .thursday(let hours):
            return ("T" ,  String(format: "%.2f", hours))
        case .friday(let hours):
            return ("F" ,  String(format: "%.2f", hours))
        }
    }
}


struct TimeSheetView: View {
    
    private let days: [Day] = [.saturday(0.0), .sunday(0.0), .monday(8.0), .tuesday(8.556), .wednesday(9.87), .thursday(0.0), .friday(0.0)]
    
    var body: some View {
        WeekView(days)
    }
}


struct ReportSheetView: View {
    var body: some View {
        Text("Report View")
    }
}

struct AccountView: View {
    var body: some View {
        Text("Account View")
    }
}
