<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Event;
use Illuminate\Http\Request;

class EventController extends Controller
{
    /**
     * Get All Events
     *
     * Display a listing of the event.
     */
    public function index()
    {
        $events = Event::all();
        return response()->json([
            'status' => true,
            'events' => $events
        ]);
    }

    /**
     * Show the form for creating a new event.
     */
    public function create()
    {
        //
    }

    /**
     * Create and Save new Event
     *
     * Store a newly created event in database.
     */
    public function store(Request $request)
    {
        $event = Event::create($request->all());

        return response()->json($event, 201);
    }

    /**
     * Get Event by ID
     *
     * Display the specified event.
     */
    public function show(Event $event)
    {
        return $event;
    }

    /**
     * Show the form for editing the specified event.
     */
    public function edit(Event $event)
    {
        //
    }

    /**
     * Update Event By ID
     *
     * Update the specified event in database.
     */
    public function update(Request $request, Event $event)
    {
        $event->update($request->all());
        return response()->json($event, 200);
    }

    /**
     * Remove Event By ID
     *
     * Remove the specified event from database.
     */
    public function destroy(Event $event)
    {
        $event->delete();
        return response()->json(null, 204);
    }
}
