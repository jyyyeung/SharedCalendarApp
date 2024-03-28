<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Share;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\Request;

class ShareController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $shares = Share::all();
        return response()->json([
            'status' => true,
            'shares' => $shares
        ]);
    }

    /**
     * Show the form for creating a new resource.
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     */
    public function store(Request $request)
    {
        $share = Share::create($request->all());

        return response()->json($share, 201);
    }

    /**
     * Display the specified resource.
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
     * Show the form for editing the specified resource.
     */
    public function edit(Share $share)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, Share $share)
    {
        $share->update($request->all());
        return response()->json($share, 200);
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy(Share $share)
    {
        $share->delete();
        return response()->json(null, 204);
    }
}
