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
        let store = TimeEntryStore(Date(),
                                   selectedProject: .constant(nil),
                                   selectedTask: .constant(nil))
        TimeEntryView(store: store)
    }
}

class TimeEntryStore: ObservableObject {
    @Published var durationField = ""
    
    @DateHandler var selectedDate: Date
    let selectedProject: Binding<String?>
    let selectedTask: Binding<String?>
    
    init(_ selectedDate: Date,
         selectedProject: Binding<String?>,
         selectedTask: Binding<String?>) {
        self.selectedDate = selectedDate
        self.selectedProject = selectedProject
        self.selectedTask = selectedTask
    }
    
    func update(to date: Date) {
        // objectWillChange required to force the body redraw
        objectWillChange.send()
        selectedDate = date
    }
    
    func validateEntry(for duration: String) {
        // TODO: Perform validation so that user must not enter any random stuff
    }
}

struct TimeEntryView: View {
    
    @ObservedObject var store: TimeEntryStore
    
    @State private var notes = ""
    @State private var isProjectTapped = false
    @State private var isTaskTapped = false
    @FocusState private var focusedField: Bool
    
    private let verticalPadding = 5.0
    private let navigationLinkWidth = 20.0
    
    // To Update the date from picker,
    private var seletedDateBinding: Binding<String> {
        Binding { store.$selectedDate }
        set: { _ = $0 }
    }
    
    private var durationFieldBinding: Binding<String> {
        Binding { store.durationField }
        set: { store.validateEntry(for: $0) }
    }
    
    var body: some View {
        List {
            projectSelectionSection
            noteSection
            durationSection
            footerView
        }
    }
    
    var projectSelectionSection: some View {
        Section {
            HStack {
                Button {
                    // TODO: Handle favirote action
                } label: {
                    Image(systemName: "star")
                        .defaultAppColor()
                        .padding(.trailing)
                        .buttonStyle(BorderlessButtonStyle())
                }
                
                VStack(alignment: .leading) {
                    HStack {
                        Button {
                            isProjectTapped = true
                        } label: {
                            HStack {
                                Text(store.selectedProject.wrappedValue ?? "")
                            }
                            .foregroundColor(.primary)
                        }
                        .buttonStyle(BorderlessButtonStyle())
                        Spacer()
                        NavigationLink(destination: ProjectListView(store: ProjectListStore(selectedProject: store.selectedProject,
                                                                                            selectedTask: store.selectedTask),
                                                                    isTaskSelected: true),
                                       isActive: $isProjectTapped) {
                            EmptyView()
                        }.frame(width: navigationLinkWidth)
                    }
                    
                    Divider().padding(.vertical, verticalPadding)
                    
                    HStack {
                        Button {
                            isTaskTapped = true
                        } label: {
                            HStack {
                                Text(store.selectedTask.wrappedValue ?? "")
                            }
                            .foregroundColor(.primary)
                        }
                        .buttonStyle(BorderlessButtonStyle())
                        
                        Spacer()
                        
                        NavigationLink(destination: ProjectTaskView(selectedTask: store.selectedTask),
                                       isActive: $isTaskTapped) {
                            EmptyView()
                        }.frame(width: navigationLinkWidth)
                    }
                }
            }
            .padding(.vertical, verticalPadding)
        }
    }
    
    var noteSection: some View {
        ZStack(alignment: .topLeading) {
            if notes.isEmpty && !focusedField {
                Text("Notes (Optional)")
                    .foregroundColor(ColorAssets.secondary.color)
                    .padding(EdgeInsets(top: 5, leading: 0, bottom: 0, trailing:0))
            }
            TextEditor(text: $notes)
                .focused($focusedField)
                .frame(height: 80)
                .multilineTextAlignment(.leading)
        }
    }
    
    var durationSection: some View {
        Section {
            VStack {
                
                HStack {
                    Text("Date")
                    Spacer()
                    Button {
                        // TODO: Handle Stepper action
                    } label: {
                        Text(seletedDateBinding.wrappedValue)
                            .foregroundColor(ColorAssets.primary.color)
                    }
                    .buttonStyle(BorderlessButtonStyle())
                }
                
                HStack {
                    Text("Duration")
                    TextField("", text: durationFieldBinding)
                        .multilineTextAlignment(.trailing)
                        .keyboardType(.decimalPad)
                }
            }
        }
    }
    
    var footerView: some View {
        HStack {
            Spacer()
            Text("Leave blank to start timer")
                .font(.footnote)
                .foregroundColor(ColorAssets.secondary.color)
            Spacer()
        }
    }
}
