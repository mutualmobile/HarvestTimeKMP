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

struct ProjectListView: View {
    
    @Environment(\.dismiss) private var dismiss
    
    @State private var searchText = ""
    
    let projects = ["iOS Department work", "iOS Department work Hyd", "iOS Department work Blore", "PTO Holidays,", "Etc"]
    
    var body: some View {
        NavigationView {
            List {
                Section("Mutual Mobile") {
                    ForEach(searchedResult, id:\.self) { item in
                        Text(item)
                    }
                }
            }
            .searchable(text: $searchText, prompt: Text("Search here..."))
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button {
                        dismiss()
                    } label: {
                        Text("Cancel")
                    }
                    
                }
            }
            .navigationTitle(Text("Projects"))
        }
    }
    
    private var searchedResult: [String] {
        if searchText.isEmpty {
            return projects
        } else {
            return projects.filter { $0.contains(searchText) }
        }
    }
}