<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Account;
use Illuminate\Http\Request;


/**
 * APIs for managing user accounts
 */
class AccountController extends Controller
{
    /**
     * Get All Accounts
     *
     * Display a listing of the account.
     */
    public function index()
    {
        $accounts = Account::all();
        return $accounts;
    }

    /**
     * Show the form for creating a new account.
     */
    public function create()
    {
        //
    }

    /**
     * Create and Save new Account
     *
     * Store a newly created account in database.
     *
     * @apiResourceModel App\Models\Account
     */
    public function store(Request $request)
    {
        $account = Account::create($request->all());

        return response()->json($account, 201);
    }

    /**
     * Get Account by ID
     *
     * Display the specified account.
     */
    public function show(Account $account)
    {
        return $account;
    }

    /**
     * Show the form for editing the specified account.
     */
    public function edit(Account $account)
    {
        //
    }

    /**
     * Update Account By ID
     *
     * Update the specified account in database.
     */
    public function update(Request $request, Account $account)
    {
        $account->update($request->all());
        return response()->json($account, 200);
    }

    /**
     * Delete Account By ID
     *
     * Remove the specified account from database.
     */
    public function destroy(Account $account)
    {
        $account->delete();
        return response()->json(null, 204);
    }
}
