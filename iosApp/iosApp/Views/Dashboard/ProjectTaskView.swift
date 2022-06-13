//
//  ProjectTaskView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 10/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct ProjectTaskView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectTaskView(selectedTask: .constant(""))
    }
}

class ProjectTaskStore: ObservableObject {
    
    var billableList = ["Work"]
    var nonBillableList = ["Non-Billable"]
    
    init() { }
}

// TODO: Searching for Project Task Item is yet to be implemented
struct ProjectTaskView: View {
    
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var store = ProjectTaskStore()
    
    @State private var searchText = ""
    var selectedTask: Binding<String?>
    
    init(selectedTask: Binding<String?>) {
        self.selectedTask = selectedTask
        UIBarButtonItem.appearance().tintColor = UIColor(ColorAssets.colorBackground.color)
    }
    
    var body: some View {
        List {
            Section("Billable") {
                ForEach(store.billableList, id: \.self) { item in
                    Button {
                        selectedTask.wrappedValue = item
                        dismiss()
                    } label: {
                        Text(item)
                            .foregroundColor(ColorAssets.primary.color)
                    }
                }
            }
            
            Section("Non-Billable") {
                ForEach(store.nonBillableList, id: \.self) { item in
                    Button {
                        selectedTask.wrappedValue = item
                        dismiss()
                    } label: {
                        Text(item)
                            .foregroundColor(ColorAssets.primary.color)
                    }
                }
            }
        }
        .searchable(text: $searchText,
                    placement: .navigationBarDrawer(displayMode: .always),
                    prompt: Text("Search for project task"))
        .navigationTitle(Text("Task"))
    }
}
