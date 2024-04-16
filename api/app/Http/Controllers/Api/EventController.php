<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Event;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;

class EventController extends Controller
{
    // /**
    //  * Get All Events
    //  *
    //  * Display a listing of the event.
    //  */
    // public function index(string $calendarId)
    // {
    //     $events = Event::where('calendarId', $calendarId)->get();
    //     // $events = Event::all();
    //     return $events;
    // }

    // /**
    //  * Create and Save new Event
    //  *
    //  * Store a newly created event in database.
    //  */
    // public function store(Request $request)
    // {
    //     $event = Event::create($request->all());

    //     return response()->json($event, 201);
    // }

    /**
     * Get Event by ID
     *
     * Display the specified event.
     */
    public function show(Event $event)
    {
        try {
            $event = Event::findOrFail($event->id);
        } catch (ModelNotFoundException $exception) {
            return back()->withError($exception->getMessage())->withInput();
        }
        return $event;
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
