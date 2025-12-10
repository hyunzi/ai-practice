# Disconnect a Financial Connections account

Use the Disconnect API to unlink customer bank accounts.

Disconnect a user’s [Financial Connections Account](https://docs.stripe.com/api/financial_connections/accounts/object.md) if you no longer need data access or if your user writes into you requesting disconnection. Alternatively, your users can [disconnect their accounts themselves](https://support.stripe.com/user/how-do-i-disconnect-my-linked-financial-account).

If you disconnect an account, you can’t refresh their data and access previously refreshed data. However, any associated [PaymentMethods](https://docs.stripe.com/api/payment_methods.md) remain usable.

To regain access to new account data, your user needs to re-authenticate their account through the [authentication flow](https://docs.stripe.com/financial-connections/fundamentals.md#authentication-flow).

## Disconnect a Financial Connections account 

To disconnect an account, use the [disconnect API](https://docs.stripe.com/api/financial_connections/accounts/disconnect.md):

```curl
curl -X POST https://api.stripe.com/v1/financial_connections/accounts/{{FINANCIALCONNECTIONSACCOUNT_ID}}/disconnect \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe financial_connections accounts disconnect {{FINANCIALCONNECTIONSACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.financial_connections.accounts.disconnect('{{FINANCIALCONNECTIONSACCOUNT_ID}}')
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.financial_connections.accounts.disconnect(
  "{{FINANCIALCONNECTIONSACCOUNT_ID}}",
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->financialConnections->accounts->disconnect(
  '{{FINANCIALCONNECTIONSACCOUNT_ID}}',
  []
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountDisconnectParams params = AccountDisconnectParams.builder().build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account =
  client.v1().financialConnections().accounts().disconnect(
    "{{FINANCIALCONNECTIONSACCOUNT_ID}}",
    params
  );
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.financialConnections.accounts.disconnect(
  '{{FINANCIALCONNECTIONSACCOUNT_ID}}'
);
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.FinancialConnectionsAccountDisconnectParams{}
result, err := sc.V1FinancialConnectionsAccounts.Disconnect(
  context.TODO(), "{{FINANCIALCONNECTIONSACCOUNT_ID}}", params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.FinancialConnections.Accounts;
Stripe.FinancialConnections.Account account = service.Disconnect(
    "{{FINANCIALCONNECTIONSACCOUNT_ID}}");
```

This request returns the account with an updated `status` to reflect the successful disconnection.

```json
{
  "id": "fca_zbyrdjTrwcYZJZc6WBs6GPid",
  "object": "financial_connections.account",
  "account_holder": {
    "customer": "cus_NfjonN9919dELB",
    "type": "customer"
  },
  "institution_name": "PNC Bank","status": "disconnected",
  // ...
}
```

After account disconnection, Stripe emits a `financial_connections.account.disconnected` *webhook* (A webhook is a real-time push notification sent to your application as a JSON payload through HTTPS requests).
