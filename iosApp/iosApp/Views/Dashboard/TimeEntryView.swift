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
        let store = TimeEntryStore(Date(), selectedProject: "", selectedTask: "")
        TimeEntryView(store: store)
    }
}

class TimeEntryStore: ObservableObject {
    @Published var durationField = ""

    @DateHandler var selectedDate: Date
    let selectedProject: String
    let selectedTask: String
    
    init(_ selectedDate: Date, selectedProject: String, selectedTask: String) {
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
    @FocusState private var focusedField: Bool
    
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
                    
                    Button {
                        // TODO: Handle Project selection action
                    } label: {
                        HStack {
                            Text(store.selectedProject)
                            Spacer()
                            Image(systemName: "chevron.right")
                        }
                        .foregroundColor(.primary)
                    }
                    .buttonStyle(BorderlessButtonStyle())
                    
                    Divider()
                    
                    Button {
                        // TODO: Handle Project type selection action
                    } label: {
                        HStack {
                            Text(store.selectedTask)
                            Spacer()
                            Image(systemName: "chevron.right")
                        }
                        .foregroundColor(.primary)
                    }
                    .buttonStyle(BorderlessButtonStyle())
                }
            }
            .padding(.vertical)
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
