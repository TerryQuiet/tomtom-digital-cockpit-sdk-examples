/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.telephony.customrecentcalls

import com.tomtom.ivi.platform.contacts.api.common.model.PhoneNumber
import com.tomtom.ivi.platform.contacts.api.common.model.PhoneNumberType
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.recentcalls.api.common.model.RecentCall
import com.tomtom.ivi.platform.recentcalls.api.common.model.RecentCall.RecentCallType
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsServiceBase
import com.tomtom.ivi.platform.telecom.api.common.model.PhoneBookSynchronizationStatus
import java.time.Duration
import java.time.Instant

/**
 * A custom recent calls service that contains two recent calls.
 */
internal class CustomRecentCallsService(iviServiceHostContext: IviServiceHostContext) :
    RecentCallsServiceBase(iviServiceHostContext) {

    // The source of recent calls.
    private val recentCallsSource = listOf(
        RecentCall(
            type = RecentCallType.INCOMING,
            displayName = "John Smith",
            phoneNumber = PhoneNumber("+281111111", PhoneNumberType.Mobile),
            duration = Duration.ofSeconds(60),
            creationTime = Instant.now()
        ),
        RecentCall(
            type = RecentCallType.OUTGOING,
            displayName = "Kelly Goodwin",
            phoneNumber = PhoneNumber("+2822222222", PhoneNumberType.Work),
            duration = Duration.ZERO,
            creationTime = Instant.now().minus(Duration.ofHours(1))
        ),
    )

    override fun onCreate() {
        super.onCreate()
        // Initialize the synchronization status.
        phoneBookSynchronizationStatus = PhoneBookSynchronizationStatus.NO_CONNECTED_DEVICES
        // Initialize the recentCallsDescending property with an empty list.
        recentCallsDescending = emptyList()
        // Set the service to ready. Now clients can call the service's APIs.
        serviceReady = true
        // The source of recent calls is ready and synchronization starts.
        phoneBookSynchronizationStatus = PhoneBookSynchronizationStatus.SYNCHRONIZATION_IN_PROGRESS
        // Update recentCallsDescending with the list of recent calls from the source.
        // Make sure that the list is in descending chronological order.
        // If a client (typically a view model) requires the list in a different order,
        // then it should resort the list before use.
        recentCallsDescending = recentCallsSource.sortedByDescending(RecentCall::creationTime)
    }

    override fun onDestroy() {
        // Put cleanup code here, if necessary.
        super.onDestroy()
    }
}
