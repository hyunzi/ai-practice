# Using manual payouts

Send manual payouts to your connected accounts.

If you set the value of [payments.payouts.schedule.interval](https://docs.stripe.com/api/balance-settings/update.md#update_balance_settings-payments-payouts-schedule-interval) to `manual`, we hold funds in the account holder’s balance until you specify otherwise. You must pay out the funds within the time period specified below, based on the business’s country:

| Country             | Holding Period |
| ------------------- | -------------- |
| Thailand            | 10 days        |
| United States       | 2 years        |
| All other countries | 90 days        |

```curl
curl https://api.stripe.com/v1/balance_settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "payments[payouts][schedule][interval]"=manual
```

```cli
stripe balance_settingss update  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "payments[payouts][schedule][interval]"=manual
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

balance_settings = client.v1.balance_settings.update(
  {payments: {payouts: {schedule: {interval: 'manual'}}}},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
balance_settings = client.v1.balance_settings.update(
  {"payments": {"payouts": {"schedule": {"interval": "manual"}}}},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$balanceSettings = $stripe->balanceSettings->update(
  ['payments' => ['payouts' => ['schedule' => ['interval' => 'manual']]]],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

BalanceSettingsUpdateParams params =
  BalanceSettingsUpdateParams.builder()
    .setPayments(
      BalanceSettingsUpdateParams.Payments.builder()
        .setPayouts(
          BalanceSettingsUpdateParams.Payments.Payouts.builder()
            .setSchedule(
              BalanceSettingsUpdateParams.Payments.Payouts.Schedule.builder()
                .setInterval(
                  BalanceSettingsUpdateParams.Payments.Payouts.Schedule.Interval.MANUAL
                )
                .build()
            )
            .build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

BalanceSettings balanceSettings =
  client.v1().balanceSettings().update(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const balanceSettings = await stripe.balanceSettings.update(
  {
    payments: {
      payouts: {
        schedule: {
          interval: 'manual',
        },
      },
    },
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.BalanceSettingsUpdateParams{
  Payments: &stripe.BalanceSettingsUpdatePaymentsParams{
    Payouts: &stripe.BalanceSettingsUpdatePaymentsPayoutsParams{
      Schedule: &stripe.BalanceSettingsUpdatePaymentsPayoutsScheduleParams{
        Interval: stripe.String(stripe.BalanceSettingsPaymentsPayoutsScheduleIntervalManual),
      },
    },
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1BalanceSettings.Update(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new BalanceSettingsUpdateOptions
{
    Payments = new BalanceSettingsPaymentsOptions
    {
        Payouts = new BalanceSettingsPaymentsPayoutsOptions
        {
            Schedule = new BalanceSettingsPaymentsPayoutsScheduleOptions
            {
                Interval = "manual",
            },
        },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.BalanceSettings;
BalanceSettings balanceSettings = service.Update(options, requestOptions);
```

> This guide shows how to configure payouts using the Balance Settings API. Use [Balance Settings](https://docs.stripe.com/api/balance-settings.md) to manage payout settings for Accounts v2. The Balance Settings object follows similar structure and behavior to the Accounts v1 `settings.payouts` hash. If you’re currently using `settings.payouts` on Accounts v1, you can continue to do so.

To trigger a payout of these funds, use the [Payouts API](https://docs.stripe.com/api/payouts/create.md). The Payouts API is only for moving funds from a connected Stripe account’s balance into their external account. To move funds between the platform and a connected account, use [separate charges and transfers](https://docs.stripe.com/connect/separate-charges-and-transfers.md) or [destination charges](https://docs.stripe.com/connect/destination-charges.md).

> *Escrow* has a precise legal definition, and Stripe doesn’t provide escrow services or support escrow accounts. However, you can control payout timing through manual payouts, which allow you to delay payouts to certain accounts. When using manual payouts, you must pay out funds within the time frame for the business’s country.
> 
> Delayed payouts can be useful when a delivery is delayed or when there’s a possibility of a refund.

## Regular payouts

The following example sends 10 USD from a connected account’s Stripe balance to their external account:

```curl
curl https://api.stripe.com/v1/payouts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d amount=1000 \
  -d currency=usd
```

```cli
stripe payouts create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --amount=1000 \
  --currency=usd
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payout = client.v1.payouts.create(
  {
    amount: 1000,
    currency: 'usd',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payout = client.v1.payouts.create(
  {"amount": 1000, "currency": "usd"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$payout = $stripe->payouts->create(
  [
    'amount' => 1000,
    'currency' => 'usd',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PayoutCreateParams params =
  PayoutCreateParams.builder().setAmount(1000L).setCurrency("usd").build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Payout payout = client.v1().payouts().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const payout = await stripe.payouts.create(
  {
    amount: 1000,
    currency: 'usd',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PayoutCreateParams{
  Amount: stripe.Int64(1000),
  Currency: stripe.String(stripe.CurrencyUSD),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Payouts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PayoutCreateOptions { Amount = 1000, Currency = "usd" };
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Payouts;
Payout payout = service.Create(options, requestOptions);
```

With a standard payout, you can move an amount up to the user’s available balance. To find that amount, perform a [retrieve balance](https://docs.stripe.com/api.md#retrieve_balance) call on their behalf.

Stripe tracks balance contributions from different payment sources in separate balances. The retrieve balance response breaks down the components of each balance by source type. For example, to create a payout specifically for a non-credit-card balance, specify the `source_type` in your request.

```curl
curl https://api.stripe.com/v1/payouts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d amount=24784 \
  -d currency=usd \
  -d source_type=bank_account
```

```cli
stripe payouts create  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  --amount=24784 \
  --currency=usd \
  --source-type=bank_account
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

payout = client.v1.payouts.create(
  {
    amount: 24784,
    currency: 'usd',
    source_type: 'bank_account',
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
payout = client.v1.payouts.create(
  {"amount": 24784, "currency": "usd", "source_type": "bank_account"},
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$payout = $stripe->payouts->create(
  [
    'amount' => 24784,
    'currency' => 'usd',
    'source_type' => 'bank_account',
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

PayoutCreateParams params =
  PayoutCreateParams.builder()
    .setAmount(24784L)
    .setCurrency("usd")
    .setSourceType(PayoutCreateParams.SourceType.BANK_ACCOUNT)
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Payout payout = client.v1().payouts().create(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const payout = await stripe.payouts.create(
  {
    amount: 24784,
    currency: 'usd',
    source_type: 'bank_account',
  },
  {
    stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
  }
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.PayoutCreateParams{
  Amount: stripe.Int64(24784),
  Currency: stripe.String(stripe.CurrencyUSD),
  SourceType: stripe.String(stripe.PayoutSourceTypeBankAccount),
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1Payouts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new PayoutCreateOptions
{
    Amount = 24784,
    Currency = "usd",
    SourceType = "bank_account",
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Payouts;
Payout payout = service.Create(options, requestOptions);
```

While individual balance components can go negative (such as through refunds or chargebacks), you can’t create payouts for greater than the aggregate available balance.
