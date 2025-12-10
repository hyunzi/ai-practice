# Payouts and top-ups from Stripe Payments

Learn how to move money between Payments account balances and financial account balances.

> #### Accounts v2 API compatibility
> 
> The Accounts v2 API doesn’t support Financial Accounts workflows. If you have accounts created with Accounts v2, you can use Accounts v1 to manage the `treasury` and `card_issuing` capabilities.

You can move money from Stripe Payments to Financial Accounts for platforms with [payouts](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md#payouts), and from Financial Accounts for platforms back to Stripe Payments with [top-ups](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md#top-ups).

## Financial accounts as external accounts 

Before you can send payouts to a financial account or receive top-ups from a financial account, you must set the financial account as an external account (BankAccount object) connected to the relevant Stripe account.

- Connected accounts: Use [POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}/external_accounts](https://docs.stripe.com/api/external_account_bank_accounts/create.md) with the financial account `routing_number` and `account_number` to create a `BankAccount` object you can use for payouts from a connected account
- Platform accounts: Use the Stripe Dashboard to create a `BankAccount` object you can use for payouts from, or top-ups to, your platform account

If there isn’t a default external account set for the `usd` currency, the financial account becomes the account’s default payout destination. If there is one, you can make the financial account the default for a connected account by setting [default_for_currency](https://docs.stripe.com/api/external_account_bank_accounts/create.md?lang=node#account_create_bank_account-default_for_currency) to true, as in the following example. If you have an automatic payout schedule enabled, that changes the target of the payouts to the financial account.

```bash
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}}/external_accounts \
  -u <<YOUR_SECRET_KEY>>: \
  -d "external_account[object]"="bank_account" \
  -d "external_account[routing_number]"="{{FINANCIAL_ACCOUNT_ROUTING_NUMBER}}" \
  -d "external_account[account_number]"="{{FINANCIAL_ACCOUNT_ACCOUNT_NUMBER}}" \
  -d "external_account[country]"="US" \
  -d "external_account[currency]"="usd" \
  -d "default_for_currency"="true"
```

If successful, the response returns the newly created `BankAccount` object.

```json
{
  "id": "{{BANK_ACCOUNT_ID}}",
  "object": "bank_account",
  "account": "{{CONNECTED_ACCOUNT_ID}}",
  "account_holder_name": null,
  "account_holder_type": null,
  "available_payout_methods": [
    "standard",
    "instant"
  ],
  ...
  "default_for_currency": true,
  ...
}
```

### Fifth Third financial accounts as external accounts

> Access to Fifth Third is currently limited to preview users. [Reach out to us](mailto:treasury-support@stripe.com) if you’re interested in trying it out.

You now have the option to set up an external account by specifying the financial account `id`. You must also request the [intra_stripe_flows](https://docs.stripe.com/api/treasury/financial_accounts/object.md#financial_account_object-features-intra_stripe_flows) feature on your Financial Account.

```bash
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}}/external_accounts \
  -u <<YOUR_SECRET_KEY>>: \
  -d "external_account[object]"="bank_account" \
  -d "external_account[financial_account]"="{{FINANCIAL_ACCOUNT_ID}}" \
  -d "external_account[country]"="US" \
  -d "external_account[currency]"="usd" \
  -d "default_for_currency"="true"
```

## Payouts

Every Stripe platform account and connected account has an account balance (also called a payments balance or acquiring balance) that stores their money. Those funds aren’t directly accessible for use outside Stripe. Payouts move funds from an account balance to an externally accessible financial account balance or external bank account.

You can fund a financial account using automatic or manual payouts. Learn more about [financial accounts](https://docs.stripe.com/financial-accounts/connect/account-management/accounts-structure.md).

To learn more about how payouts work with Connect, see the [Pay out money guide](https://docs.stripe.com/connect/add-and-pay-out-guide.md).

## Automatic payouts

If you have connected accounts on your platform with an automatic payout schedule, you can change the target of direct payouts from their external bank accounts to their financial accounts. It requires the financial account to be set up as a `BankAccount` object; see the [Financial accounts as external accounts](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md#financial-accounts-as-external-accounts) section for more information.

## Automatic payout speeds

By default, when using automatic payouts, funds from incoming card payments become available in the financial account balance two business days after the payment is received (T+2 schedule). Other payment types, such as *ACH* (Automated Clearing House (ACH) is a US financial network used for electronic payments and money transfers that doesn’t rely on paper checks, credit card networks, wire transfers, or cash) payments take longer.

After a platform-level credit approval, you can enable T+1 faster payouts on a per-account basis for connected accounts. You can set the `interval` and `delay_days` parameters with `POST /v1/accounts` to accelerate payout schedules automatically (or you can manually trigger faster payouts as described below). Contact [treasury-support@stripe.com](mailto:treasury-support@stripe.com) if you want to request access to faster payouts (manual, automated, or both) for the connected accounts on your platform.

T+1 faster payout schedules apply to all payment types, including card payments and ACH payments.

### T+1 automatic payout schedule

To have incoming payments automatically available in the financial account on the next business day, send a request with `interval` set to `daily` and `delay_days` set to `1`.

Use [POST /v1/accounts/{{CONNECTED_ACCOUNT_ID}}](https://docs.stripe.com/api/accounts/object.md#account_object-settings-payouts-schedule) to update the payout schedule.

```curl
curl https://api.stripe.com/v1/accounts/{{CONNECTED_ACCOUNT_ID}} \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "settings[payouts][schedule][interval]"=daily \
  -d "settings[payouts][schedule][delay_days]"=1
```

```cli
stripe accounts update {{CONNECTED_ACCOUNT_ID}} \
  -d "settings[payouts][schedule][interval]"=daily \
  -d "settings[payouts][schedule][delay_days]"=1
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    settings: {
      payouts: {
        schedule: {
          interval: 'daily',
          delay_days: 1,
        },
      },
    },
  },
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.update(
  "{{CONNECTED_ACCOUNT_ID}}",
  {"settings": {"payouts": {"schedule": {"interval": "daily", "delay_days": 1}}}},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->update(
  '{{CONNECTED_ACCOUNT_ID}}',
  [
    'settings' => [
      'payouts' => [
        'schedule' => [
          'interval' => 'daily',
          'delay_days' => 1,
        ],
      ],
    ],
  ]
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountUpdateParams params =
  AccountUpdateParams.builder()
    .setSettings(
      AccountUpdateParams.Settings.builder()
        .setPayouts(
          AccountUpdateParams.Settings.Payouts.builder()
            .setSchedule(
              AccountUpdateParams.Settings.Payouts.Schedule.builder()
                .setInterval(AccountUpdateParams.Settings.Payouts.Schedule.Interval.DAILY)
                .setDelayDays(1L)
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().update("{{CONNECTED_ACCOUNT_ID}}", params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.update(
  '{{CONNECTED_ACCOUNT_ID}}',
  {
    settings: {
      payouts: {
        schedule: {
          interval: 'daily',
          delay_days: 1,
        },
      },
    },
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountUpdateParams{
  Settings: &stripe.AccountUpdateSettingsParams{
    Payouts: &stripe.AccountUpdateSettingsPayoutsParams{
      Schedule: &stripe.AccountUpdateSettingsPayoutsScheduleParams{
        Interval: stripe.String(stripe.AccountSettingsPayoutsScheduleIntervalDaily),
        DelayDays: stripe.Int64(1),
      },
    },
  },
}
result, err := sc.V1Accounts.Update(context.TODO(), "{{CONNECTED_ACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountUpdateOptions
{
    Settings = new AccountSettingsOptions
    {
        Payouts = new AccountSettingsPayoutsOptions
        {
            Schedule = new AccountSettingsPayoutsScheduleOptions
            {
                Interval = "daily",
                DelayDays = 1,
            },
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Update("{{CONNECTED_ACCOUNT_ID}}", options);
```

The response confirms the updated settings.

```json
{
  ...
  "settings": {
    "payouts": {
      "schedule": {
        "interval": "daily",
        "delay_days": 1,
      },
    },
    ...
  }
  ...
}
```

## Manual payouts

You can use manual payouts to send specific portions of an account balance to that account’s financial account.

Use [POST /v1/payouts](https://docs.stripe.com/api/payouts/create.md) with the bank account `id` associated with your financial account specified as the `destination` parameter value. If you don’t have a `BankAccount` object associated with your financial account yet, see the [Financial accounts as external accounts](https://docs.stripe.com/financial-accounts/connect/moving-money/payouts.md#financial-accounts-as-external-accounts) section for more information.

```curl
curl https://api.stripe.com/v1/payouts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d method=standard \
  -d amount=1000 \
  -d currency=usd \
  -d destination={{BANK_ACCOUNT_ID}}
```

```cli
stripe payouts create  \
  --method=standard \
  --amount=1000 \
  --currency=usd \
  --destination={{BANK_ACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payout = client.v1.payouts.create({
  method: 'standard',
  amount: 1000,
  currency: 'usd',
  destination: '{{BANK_ACCOUNT_ID}}',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payout = client.v1.payouts.create({
  "method": "standard",
  "amount": 1000,
  "currency": "usd",
  "destination": "{{BANK_ACCOUNT_ID}}",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$payout = $stripe->payouts->create([
  'method' => 'standard',
  'amount' => 1000,
  'currency' => 'usd',
  'destination' => '{{BANK_ACCOUNT_ID}}',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PayoutCreateParams params =
  PayoutCreateParams.builder()
    .setMethod(PayoutCreateParams.Method.STANDARD)
    .setAmount(1000L)
    .setCurrency("usd")
    .setDestination("{{BANK_ACCOUNT_ID}}")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Payout payout = client.v1().payouts().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const payout = await stripe.payouts.create({
  method: 'standard',
  amount: 1000,
  currency: 'usd',
  destination: '{{BANK_ACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PayoutCreateParams{
  Method: stripe.String("standard"),
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Destination: stripe.String("{{BANK_ACCOUNT_ID}}"),
}
result, err := sc.V1Payouts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PayoutCreateOptions
{
    Method = "standard",
    Amount = 1000,
    Currency = "usd",
    Destination = "{{BANK_ACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Payouts;
Payout payout = service.Create(options);
```

If successful, the response returns the newly created [Payout object](https://docs.stripe.com/api/payouts/object.md).

```json
{
  "id": "{{PAYOUT_ID}}",
  "object": "payout",
  "amount": 1000,
  ...
  "currency": "usd",
  "destination": "{{BANK_ACCOUNT_ID}}",
  ...
  "method": "standard",
  ...
}
```

## Manual payout speeds

Two speed methods are available for manual payouts - `standard` and `instant`.

Manual payouts using `standard` speed can only draw on the `available` balance, and funds will generally arrive in the financial account within 2 hours.

If your platform is enabled for faster payouts, you can use `instant` manual payouts to move funds to a connected account’s financial account within 30 minutes. You can request instant manual payouts at any time, including weekends and holidays, and instant manual payouts can draw on the `instant_available` balance of a Stripe account. If your platform is not enabled for faster payouts and you’re interested in adding the ability, contact [treasury-support@stripe.com](mailto:treasury-support@stripe.com) to request the feature.

To request an instant manual payout rather than a standard manual payout, change the `method` parameter in the payout request from `standard` to `instant`.

## Testing payouts

You can simulate a payout to a connected account financial account using API keys from a testing environment.

You can only simulate a payout to a platform financial account using the Dashboard in testing environments. Click the **Add money** button in the upper right of the [**Balances**](https://dashboard.stripe.com/test/treasury) view of your Dashboard to open the **Add Money** dialog. Follow the prompts to simulate sending money from your platform account balance to your financial account balance.

## Top-ups

As the owner of a *Connect* (Connect is Stripe's solution for multi-party businesses, such as marketplace or software platforms, to route payments between sellers, customers, and other recipients) platform, you can use an eligible financial account balance to top-up your platform account balance instead of using an external bank account. Your financial account must have the [financial_addresses.aba](https://docs.stripe.com/financial-accounts/connect/account-management/financial-account-features.md#available-features) feature active to be eligible. To top-up a platform account balance, you must set the platform’s financial account as the platform’s default external `BankAccount` using the Stripe Dashboard as described in the [Adding funds to your platform balance](https://docs.stripe.com/connect/top-ups.md) guide.

Unlike true external bank accounts, new `BankAccount` objects with financial account details owned by the merchant with the BankAccount object are automatically verified upon creation so they don’t require verification with microdeposits.

After you set the financial account as your platform’s default external bank account, use `POST /v1/topups` to create the top-up.

```curl
curl https://api.stripe.com/v1/topups \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d amount=2000 \
  -d currency=usd \
  -d description=Top-up \
  -d statement_descriptor=Top-up
```

```cli
stripe topups create  \
  --amount=2000 \
  --currency=usd \
  --description=Top-up \
  --statement-descriptor=Top-up
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

topup = client.v1.topups.create({
  amount: 2000,
  currency: 'usd',
  description: 'Top-up',
  statement_descriptor: 'Top-up',
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
topup = client.v1.topups.create({
  "amount": 2000,
  "currency": "usd",
  "description": "Top-up",
  "statement_descriptor": "Top-up",
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$topup = $stripe->topups->create([
  'amount' => 2000,
  'currency' => 'usd',
  'description' => 'Top-up',
  'statement_descriptor' => 'Top-up',
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

TopupCreateParams params =
  TopupCreateParams.builder()
    .setAmount(2000L)
    .setCurrency("usd")
    .setDescription("Top-up")
    .setStatementDescriptor("Top-up")
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Topup topup = client.v1().topups().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const topup = await stripe.topups.create({
  amount: 2000,
  currency: 'usd',
  description: 'Top-up',
  statement_descriptor: 'Top-up',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TopupCreateParams{
  Amount: stripe.Int64(2000),
  Currency: stripe.String(stripe.CurrencyUSD),
  Description: stripe.String("Top-up"),
  StatementDescriptor: stripe.String("Top-up"),
}
result, err := sc.V1Topups.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new TopupCreateOptions
{
    Amount = 2000,
    Currency = "usd",
    Description = "Top-up",
    StatementDescriptor = "Top-up",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Topups;
Topup topup = service.Create(options);
```

If successful, the response returns the `Top-Up` object.

```json
{
  "id": "{{TOP_UP_ID}}",
  "object": "topup",
  "amount": 2000,
  "currency": "usd",
  "description": "Top-up",
  ...
}
```

Learn more about [adding funds to your platform account balance](https://docs.stripe.com/connect/top-ups.md) with Stripe Connect.
