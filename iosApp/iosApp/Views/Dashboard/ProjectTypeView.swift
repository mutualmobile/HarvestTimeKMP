//
//  ProjectTypeView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 10/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct ProjectTypeView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectTypeView(selectedItem: .constant(""))
    }
}

class ProjectTypeStore: ObservableObject {
    
    var billableList = ["Work"]
    var nonBillableList = ["Non-Billable"]
    
    init() { }
}

struct ProjectTypeView: View {
    
    @Environment(\.dismiss) private var dismiss
    @ObservedObject var store = ProjectTypeStore()
    
    @Binding var selectedItem: String?
    
    var body: some View {
        List {
            Section("Billable") {
                ForEach(store.billableList, id: \.self) { item in
                    Button {
                        selectedItem = item
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
                        selectedItem = item
                        dismiss()
                    } label: {
                        Text(item)
                            .foregroundColor(ColorAssets.primary.color)
                    }
                }
            }
        }
    }
}
