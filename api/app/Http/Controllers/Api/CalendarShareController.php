<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Calendar;
use App\Models\Share;
use Illuminate\Http\Request;

class CalendarShareController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index(Calendar $calendar)
    {
        $shares = Share::where('calendarId', $calendar->id)->get();
        return $shares;
    }


    /**
     * Share Calendar
     *
     * Share the specified calendar with another user.
     * @param Request $request
     * @param Calendar $calendar
     */
    public function store(Request $request, Calendar $calendar)
    {
        $authorized = false;
        // Authenticated user is not the owner of the calendar
        $calendar->ownerId === auth()->id() ?: $authorized = true;
        // Authenticated User has not been shared the calendar
        $share = Share::where('calendarId', $calendar->id)->where('userId', auth()->id());
        if ($share->exists()) {
            // Authenticated User has write permission
            $share->getAttribute('permission') === 'ADMIN' ?: $authorized = true;
        }
        if (!$authorized) {
            return response()->json(['error' => 'Unauthorized'], 403);
        }
        $share = Share::create([
            // taken from parent URL API
            'calendarId' => $calendar->id,
            // taken from request
            'userId' => $request->userId,
            'permission' => $request->permission,
        ]);

        // 201 Created
        return response()->json($share, 201);
    }

    /**
     * Display the specified resource.
     */
    public function show(string $id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, string $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(string $id)
    {
        //
    }
}
