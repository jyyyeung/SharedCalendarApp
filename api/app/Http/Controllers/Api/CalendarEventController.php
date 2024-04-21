<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Event;
use Illuminate\Http\Request;

class CalendarEventController extends Controller
{
    /**
     * Get All Events
     *
     * Display a listing of the event.
     */
    public function index(string $calendarId)
    {
        $events = Event::where('calendarId', $calendarId)->get();
        return $events;
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request, string $calendarId)
    {
        // TODO: Validate request

        $details = $request->all();
        $details['calendarId'] = $calendarId;
        $event = Event::create($details);
        $event->save();

        return response()->json($event, 201);
    }
}
