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
    
    init() { }
}

struct ProjectListView: View {
    
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var store = ProjectListStore()

    @State private var selectedItem: String?
    @State private var searchText = ""
    
    var body: some View {
        NavigationView {
            List {
                Section("Mutual Mobile") {
                    
                    ForEach(searchedResult, id:\.self) { item in
                        NavigationLink {
                            ProjectTypeView(selectedItem: $selectedItem)
                        } label: {
                            Text(item)
                        }
                    }
                }
            }
            .searchable(text: $searchText, placement: .navigationBarDrawer(displayMode: .always), prompt: Text("Search here..."))
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button {
                        dismiss()
                    } label: {
                        Text("Cancel")
                            .defaultAppColor()
                    }
                    
                }
            }
            .navigationTitle(Text("Projects"))
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
