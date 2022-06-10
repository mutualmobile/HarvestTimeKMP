//
//  ProjectListView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 09/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import Foundation
import SwiftUI

struct ProjectListView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectListView()
    }
}

class ProjectListStore: ObservableObject {
    let projects = ["iOS Department work", "iOS Department work Hyd", "iOS Department work Blore", "PTO Holidays,", "Etc"]
    
    @Published var selectedProject: String?
    @Published var selectedTask: String?
    
    var isSelected: Bool {
        selectedTask != nil && selectedProject != nil
    }
    
    init() { }
}

struct ProjectListView: View {
    
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var store = ProjectListStore()

    @State private var searchText = ""
    
    var body: some View {
        NavigationView {
            VStack {
                if store.isSelected {
                    TimeEntryView(selectedProject: store.selectedProject ?? "",
                                  selectedTask: store.selectedTask ?? "")
                } else {
                    List {
                        Section("Mutual Mobile") {
                            
                            ForEach(searchedResult, id:\.self) { project in
                                NavigationLink {
                                    ProjectTaskView(selectedProject: $store.selectedProject,
                                                    selectedTask: $store.selectedTask,
                                                    project: project)
                                } label: {
                                    Text(project)
                                }
                            }
                        }
                    }
                    .searchable(text: $searchText,
                                placement: .navigationBarDrawer(displayMode: .always),
                                prompt: Text("Search here..."))
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
                    ? nil
                    : Button(action: {
                        // TODO: Handle Start action
                    }, label: {
                        Text("Start")
                            .defaultAppColor()
                    })
                    
                }
            }
            .navigationTitle(store.isSelected
                             ? Text("Projects")
                             : Text("New Time Entry"))
        }
    }
    
    private var searchedResult: [String] {
        if searchText.isEmpty {
            return store.projects
        } else {
            return store.projects.filter { $0.contains(searchText) }
        }
    }
}
