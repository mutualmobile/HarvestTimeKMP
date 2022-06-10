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
        TimeEntryView()
    }
}

class TimeEntryStore: ObservableObject {
    
    init() {}
}

struct TimeEntryView: View {
    
    @ObservedObject private var store = TimeEntryStore()
    
    var body: some View {
        
        Form {
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
                                Text("iOS Department work Hyd")
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
                                Text("Non-Billable")
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
    }
}
