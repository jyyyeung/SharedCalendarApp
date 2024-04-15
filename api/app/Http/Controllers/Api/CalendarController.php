<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Calendar;
use App\Models\Share;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;

/**
 * APIs for managing calendars
 */
class CalendarController extends Controller
{
    /**
     * Get All Calendars
     *
     * Display a listing of the calendar.
     * @return \Illuminate\Database\Eloquent\Collection<int, \App\Models\Calendar>
     */
    public function index()
    {
        $authUser = auth()->user();
        $calendars = Calendar::where('ownerId', $authUser->id)->get();
        $sharedCalendars = Share::where('userId', $authUser->id)->get();
        foreach ($sharedCalendars as $sharedCalendar) {
            $calendars->push(Calendar::find($sharedCalendar->calendarId));
        }
        return $calendars;
    }

    /**
     * Get all calendars owned by the authenticated user
     * @return \Illuminate\Database\Eloquent\Collection<int, \App\Models\Calendar>
     */
    public function indexOwned()
    {
        $authUser = auth()->user();
        try {
            $calendars = Calendar::where('ownerId', $authUser->id)->get();
        } catch (ModelNotFoundException $exception) {
            return back()->withError($exception->getMessage())->withInput();
        }
        // $calendars = Calendar::where('ownerId', $authUser->id)->get();
        return response()->json($calendars, 200);
    }

    /**
     * Get All Shared calendars
     */
    public function indexShared()
    {
        $authUser = auth()->user();
        $sharedCalendars = Share::where('userId', $authUser->id)->get();
        $calendars = [];
        foreach ($sharedCalendars as $sharedCalendar) {
            $calendars[] = Calendar::find($sharedCalendar->calendarId);
        }

        // 200 OK
        return $calendars;
    }

    /**
     * Create and Save new Calendar
     *
     * Store a newly created calendar in database.
     * @param Request $request
     * @return \Illuminate\Database\Eloquent\Collection<int, \App\Models\Calendar>
     */
    public function store(Request $request)
    {
        $request->validate([
            'name' => 'required|string',
            'color' => 'required|string',
            'timezone' => 'required|string',
        ]);

        $calendar = Calendar::create([
            // taken from authenticated user
            'ownerId' => $request->user()->id,
            // taken from request
            'name' => $request->name,
            'color' => $request->color,
            'timezone' => $request->timezone,
        ]);

        //  201 Created
        return response()->json($calendar, 201);
    }

    /**
     * Get calendar by ID
     *
     * Display the specified calendar.
     */
    public function show(Calendar $calendar)
    {

        $authorized = false;
        // Authenticated user is not the owner of the calendar
        $calendar->ownerId === auth()->id() ?: $authorized = true;
        // Authenticated User has not been shared the calendar
        Share::where('calendarId', $calendar->id)->where('userId', auth()->id())->exists() ?: $authorized = true;

        // Check if authorized to view calendar
        if (!$authorized) {
            // 403 Forbidden
            return response()->json(['error' => 'Unauthorized'], 403);
        }

        try {
            $calendar = Calendar::findOrFail($calendar->id);
        } catch (ModelNotFoundException $exception) {
            return back()->withError($exception->getMessage())->withInput();
        }
        // 200 OK
        return response()->json($calendar, 200);
    }

    /**
     * Update Calendar By ID
     *
     * Update the specified calendar in database.
     */
    public function update(Request $request, Calendar $calendar)
    {
        $request->validate([
            'name' => 'required|string',
            'color' => 'required|string',
            'timezone' => 'required|string',
        ]);

        // Check if authenticated user is the owner of the calendar
        $authorized = false;
        // Authenticated user is not the owner of the calendar
        $calendar->ownerId === auth()->id() ?: $authorized = true;
        $share = Share::where('calendarId', $calendar->id)->where('userId', auth()->id());
        // Authenticated User has not been shared the calendar
        if ($share->exists()) {
            // Authenticated User has write permission
            $share->getAttribute('permission') === 'WRITE' ?: $authorized = true;
            $share->getAttribute('permission') === 'ADMIN' ?: $authorized = true;
        }

        if (!$authorized) {
            return response()->json(['error' => 'Unauthorized'], 403);
        }

        $calendar->update([
            'name' => $request->name,
            'color' => $request->color,
            'timezone' => $request->timezone,
        ]);

        // 200 OK
        return response()->json($calendar, 200);
    }

    /**
     * Remove Calendar By ID
     *
     * Remove the specified calendar from database.
     * @param Calendar $calendar
     * @return \Illuminate\Http\JsonResponse
     */
    public function destroy(Calendar $calendar)
    {
        // Authenticated user is not the owner of the calendar
        if ($calendar->ownerId !== auth()->id()) {
            return response()->json(['error' => 'Unauthorized'], 403);
        }

        $calendar->delete();


        return response()->json(null, 204);
    }
}
