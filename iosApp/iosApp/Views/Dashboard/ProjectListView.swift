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
        ProjectListView(store: ProjectListStore(selectedProject: .constant(nil),
                                                selectedTask: .constant(nil)))
    }
}

class ProjectListStore: ObservableObject {
    let projects = ["iOS Department work", "iOS Department work Hyd", "iOS Department work Blore", "PTO Holidays,", "Etc"]
    
    var selectedProject: Binding<String?>
    var selectedTask: Binding<String?>
    
    init(selectedProject: Binding<String?>, selectedTask: Binding<String?>) {
        self.selectedProject = selectedProject
        self.selectedTask = selectedTask
    }
}

struct ProjectListView: View {
    
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var store: ProjectListStore
    
    @State private var searchText = ""
    
    var body: some View {
        VStack {
            List {
                Section("Mutual Mobile") {
                    
                    ForEach(searchedResult, id:\.self) { project in
                        NavigationLink {
                            ProjectTaskView(selectedProject: store.selectedProject,
                                            selectedTask: store.selectedTask,
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
    
    private var searchedResult: [String] {
        if searchText.isEmpty {
            return store.projects
        } else {
            return store.projects.filter { $0.contains(searchText) }
        }
    }
}
