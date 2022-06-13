//
//  TimeEntryContainerView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 13/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct TimeEntryContainerView_Previews: PreviewProvider {
    static var previews: some View {
        TimeEntryContainerView()
    }
}

class TimeEntryContainerStore: ObservableObject {
    
    @Published var selectedProject: String?
    @Published var selectedTask: String?
    
    var isSelected: Bool {
        selectedTask != nil && selectedProject != nil
    }
    
    init() { }
}

struct TimeEntryContainerView: View {
    
    @Environment(\.dismiss) private var dismiss
    @StateObject private var store = TimeEntryContainerStore()
    
    var body: some View {
        NavigationView {
            VStack {
                if store.isSelected {
                    // TODO: Pass selected data here
                    let timEntryStore = TimeEntryStore(Date(),
                                                       selectedProject: store.selectedProject ?? "",
                                                       selectedTask: store.selectedTask ?? "")
                    TimeEntryView(store: timEntryStore)
                } else {
                    ProjectListView(store: ProjectListStore(selectedProject: $store.selectedProject,
                                                            selectedTask: $store.selectedTask))
                }
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button {
                        dismiss()
                    } label: {
                        Text("Cancel")
                            .defaultAppColor()
                    }
                }
                
                ToolbarItem(placement: .confirmationAction) {
                    return store.isSelected
                    ? Button(action: {
                        // TODO: Handle Start action
                    }, label: {
                        Text("Start")
                            .defaultAppColor()
                    }) : nil
                }
            }
            .navigationTitle(store.isSelected
                             ? Text("Projects")
                             : Text("New Time Entry"))
        }
    }
}
