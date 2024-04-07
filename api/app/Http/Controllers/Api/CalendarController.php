<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Calendar;
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
     */
    public function index()
    {
        $calendars = Calendar::all();
        return $calendars;
    }

    /**
     * Show the form for creating a new calendar.
     */
    public function create()
    {
        //
    }

    /**
     * Create and Save new Calendar
     *
     * Store a newly created calendar in database.
     */
    public function store(Request $request)
    {


        $calendar = Calendar::create($request->all());

        return response()->json($calendar, 201);
    }

    /**
     * Get calendar by ID
     *
     * Display the specified calendar.
     */
    public function show(Calendar $calendar)
    {
        try {
            $calendar = Calendar::findOrFail($calendar->id);
        } catch (ModelNotFoundException $exception) {
            return back()->withError($exception->getMessage())->withInput();
        }
        return $calendar;
    }

    /**
     * Show the form for editing the specified calendar.
     */
    public function edit(Calendar $calendar)
    {
        //
    }

    /**
     * Update Calendar By ID
     *
     * Update the specified calendar in database.
     */
    public function update(Request $request, Calendar $calendar)
    {
        $calendar->update($request->all());

        return response()->json($calendar, 200);
    }

    /**
     * Remove Calendar By ID
     *
     * Remove the specified calendar from database.
     */
    public function destroy(Calendar $calendar)
    {
        $calendar->delete();

        return response()->json(null, 204);
    }
}
