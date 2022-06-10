//
//  TimeEntryView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 10/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct TimeEntryView_Previews: PreviewProvider {
    static var previews: some View {
        TimeEntryView()
    }
}

class TimeEntryStore: ObservableObject {
    
    init() {}
}

struct TimeEntryView: View {
    
    @ObservedObject private var store = TimeEntryStore()
    
    var body: some View {
        Text("Time Entry Editor")
    }
}
