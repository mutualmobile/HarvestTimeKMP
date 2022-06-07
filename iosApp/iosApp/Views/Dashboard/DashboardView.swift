//
//  DashboardView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

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


struct TimeSheetView: View {
    var body: some View {
        Text("Timesheet View")
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
