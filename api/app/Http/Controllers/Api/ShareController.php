<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Share;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;

/**
 * APIs for managing shares
 */
class ShareController extends Controller
{
    /**
     * Get All Shares
     *
     * Display a listing of the share.
     */
    public function index()
    {
        $shares = Share::all();
        return $shares;
    }

    /**
     * Show the form for creating a new share.
     */
    public function create()
    {
        //
    }

    /**
     * Create and Save new Share
     *
     * Store a newly created share in database.
     */
    public function store(Request $request)
    {
        $share = Share::create($request->all());

        return response()->json($share, 201);
    }

    /**
     * Get Share by ID
     *
     * Display the specified share.
     */
    public function show(Share $share)
    {
        try {
            $share = Share::findOrFail($share->id);
        } catch (ModelNotFoundException $exception) {
            return back()->withError($exception->getMessage())->withInput();
        }
        return $share;
    }

    /**
     * Show the form for editing the specified share.
     */
    public function edit(Share $share)
    {
        //
    }

    /**
     * Update Share By ID
     *
     * Update the specified share in database.
     */
    public function update(Request $request, Share $share)
    {
        $share->update($request->all());
        return response()->json($share, 200);
    }

    /**
     * Remove Share By ID
     *
     * Remove the specified share from database.
     */
    public function destroy(Share $share)
    {
        $share->delete();
        return response()->json(null, 204);
    }
}
