//
//  ProjectSelection.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI


struct Projects : View {
    @State var selectedProject = ""
    var projects = ["Project 1","Project 2"]
    var body: some View{
        Menu {
            ForEach (projects, id: \.self) { project in
                Button {
                    selectedProject = project
                } label: {
                    Text(project)
                    Image(systemName: "arrow.down.right.circle")
                }
            }
        } label: {
            if selectedProject.count == 0 {
                Text("Please select a project").bold().padding()
            } else{
                Text("Project : \(selectedProject)").bold().padding()
            }
            Image(systemName: "tag.circle").padding()
        }
    }
}
