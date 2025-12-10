# Use the Settings API to configure Stripe Tax

Learn how to configure tax settings, and check whether an account is ready to perform tax calculations.

The [Stripe Tax Settings API](https://docs.stripe.com/api/tax/settings.md) lets you retrieve and configure the settings required to calculate tax without relying on the [Stripe Dashboard](https://docs.stripe.com/tax/set-up.md).

- **Connect platform**: As a platform, you can use this API to set up your connected accounts to use Stripe Tax, or to validate whether an account is already set up appropriately.
- **Direct usage**: You can use this API to set up Stripe Tax, or to validate whether you’re already set up appropriately.

# Connect platform

> This is a Connect platform for when tax-integration is connect-platform. View the full page at https://docs.stripe.com/tax/settings-api?tax-integration=connect-platform.

## Check if the connected account is ready to use Stripe Tax 

Complete this check when the Standard account configures Stripe Tax through the Stripe Dashboard but your platform needs to assess if Stripe Tax can be enabled.

Use our official libraries for access to the Stripe API from your application. To check the Stripe Tax settings on the connected account, [retrieve the `tax.settings` object](https://docs.stripe.com/api/tax/settings/retrieve.md) using the `Stripe-Account` header with a value of the connected account ID:

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}"
```

```cli
stripe tax settings retrieve  \
  --stripe-account {{CONNECTEDACCOUNT_ID}}
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.retrieve(
  {},
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
settings = client.v1.tax.settings.retrieve(
  options={"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->retrieve(
  [],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsRetrieveParams params = SettingsRetrieveParams.builder().build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Settings settings = client.v1().tax().settings().retrieve(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.retrieve({
  stripeAccount: '{{CONNECTEDACCOUNT_ID}}',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxSettingsRetrieveParams{}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxSettings.Retrieve(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.SettingsGetOptions();
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Get(options, requestOptions);
```

You can also listen to the [tax.settings.updated](https://docs.stripe.com/api/events/types.md#event_types-tax.settings.updated) webhook event which triggers when accounts update their tax settings or when new required tax settings are introduced. See [take webhooks live](https://docs.stripe.com/webhooks.md#register-webhook) to learn how to add a webhook endpoint, and make sure you select **Listen to events on Connected accounts** in the Dashboard.

An account is ready to use Stripe Tax if the response `tax.settings` object retrieved by the API or webhook event returns `"active"` for `status`. The `defaults.tax_code` and `defaults.tax_behavior` settings are only required if not provided in the product or price on each API call.

```json
{
  "object": "tax.settings",
  "defaults": {
    "tax_code": null,
    "tax_behavior": null
  },
  "head_office": {
    "address": {
      "country": "DE"
    }
  },
  "livemode": false,"status": "active",
  "status_details": {
    "active": {}
  }
}
```

An account isn’t ready to use Stripe Tax if the response `tax.settings` object returns `"pending"` for `status`. The [status_details[pending][missing_fields]](https://docs.stripe.com/api/tax/settings/object.md#tax_settings_object-status_details-pending-missing_fields) has a list of all required missing fields.

```json
{
  "object": "tax.settings",
  "defaults": {
    "tax_code": null,
    "tax_behavior": null
  },
  "head_office": null,
  "livemode": false,"status": "pending",
  "status_details": {
    "pending": {
      "missing_fields": ["head_office"]
    }
  }
}
```

## Configure connected account settings 

Complete this step when you manage all Stripe Tax configuration through an interface on your platform.

You can modify the connected account settings through an [update settings](https://docs.stripe.com/api/tax/settings/update.md) call. Perform a call providing the head office location, the preset tax code, and the tax behavior by using the `Stripe-Account` header with a value of the connected account ID.

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -H "Stripe-Account: {{CONNECTEDACCOUNT_ID}}" \
  -d "defaults[tax_code]"=txcd_10000000 \
  -d "defaults[tax_behavior]"=inclusive \
  -d "head_office[address][country]"=DE
```

```cli
stripe tax settings update  \
  --stripe-account {{CONNECTEDACCOUNT_ID}} \
  -d "defaults[tax_code]"=txcd_10000000 \
  -d "defaults[tax_behavior]"=inclusive \
  -d "head_office[address][country]"=DE
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.update(
  {
    defaults: {
      tax_code: 'txcd_10000000',
      tax_behavior: 'inclusive',
    },
    head_office: {address: {country: 'DE'}},
  },
  {stripe_account: '{{CONNECTEDACCOUNT_ID}}'},
)
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
settings = client.v1.tax.settings.update(
  {
    "defaults": {"tax_code": "txcd_10000000", "tax_behavior": "inclusive"},
    "head_office": {"address": {"country": "DE"}},
  },
  {"stripe_account": "{{CONNECTEDACCOUNT_ID}}"},
)
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->update(
  [
    'defaults' => [
      'tax_code' => 'txcd_10000000',
      'tax_behavior' => 'inclusive',
    ],
    'head_office' => ['address' => ['country' => 'DE']],
  ],
  ['stripe_account' => '{{CONNECTEDACCOUNT_ID}}']
);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsUpdateParams params =
  SettingsUpdateParams.builder()
    .setDefaults(
      SettingsUpdateParams.Defaults.builder()
        .setTaxCode("txcd_10000000")
        .setTaxBehavior(SettingsUpdateParams.Defaults.TaxBehavior.INCLUSIVE)
        .build()
    )
    .setHeadOffice(
      SettingsUpdateParams.HeadOffice.builder()
        .setAddress(
          SettingsUpdateParams.HeadOffice.Address.builder().setCountry("DE").build()
        )
        .build()
    )
    .build();

RequestOptions requestOptions =
  RequestOptions.builder().setStripeAccount("{{CONNECTEDACCOUNT_ID}}").build();
// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.

Settings settings = client.v1().tax().settings().update(params, requestOptions);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.update(
  {
    defaults: {
      tax_code: 'txcd_10000000',
      tax_behavior: 'inclusive',
    },
    head_office: {
      address: {
        country: 'DE',
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
params := &stripe.TaxSettingsUpdateParams{
  Defaults: &stripe.TaxSettingsUpdateDefaultsParams{
    TaxCode: stripe.String("txcd_10000000"),
    TaxBehavior: stripe.String(stripe.TaxSettingsDefaultsTaxBehaviorInclusive),
  },
  HeadOffice: &stripe.TaxSettingsUpdateHeadOfficeParams{
    Address: &stripe.AddressParams{Country: stripe.String("DE")},
  },
}
params.SetStripeAccount("{{CONNECTEDACCOUNT_ID}}")
result, err := sc.V1TaxSettings.Update(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.SettingsUpdateOptions
{
    Defaults = new Stripe.Tax.SettingsDefaultsOptions
    {
        TaxCode = "txcd_10000000",
        TaxBehavior = "inclusive",
    },
    HeadOffice = new Stripe.Tax.SettingsHeadOfficeOptions
    {
        Address = new AddressOptions { Country = "DE" },
    },
};
var requestOptions = new RequestOptions
{
    StripeAccount = "{{CONNECTEDACCOUNT_ID}}",
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Update(options, requestOptions);
```

The updated `tax.settings` object now has a head office, a preset tax code, and a default tax behavior, which allows you to enable Stripe Tax for this connected account.

```json
{
  "object": "tax.settings","defaults": {
    "tax_code": "txcd_10000000",
    "tax_behavior": "inclusive"
  },
  "head_office": {
    "address": {
      "country": "DE"
    }
  },
  "livemode": false,
  "status": "active",
  "status_details": {
    "active": {}
  }
}
```

### Validations and errors

The tax codes must refer to [available tax codes](https://docs.stripe.com/tax/tax-codes.md) and the [tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-behavior) must be set as `inclusive`, `exclusive`, or `inferred_by_currency` (after being set, it can’t be set to null). The `head_office` must include a supported address.

The `head_office[address]` has the fields `line1`, `line2`, `city`, `state`, `postal_code`, and `country`. The tables below describe the supported address formats.

#### United States

| Example addresses                                                                                                                         | Explanation                                                                                                                                                                                                                                                                                                                                                                                                 | Supported       |
| ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: 27 Fredrick Ave
  - `city`: Brothers
  - `state`: OR
  - `postal_code`: 97712
  - `country`: US                                | **Full address**

  A full address includes at least a line1 (street address or PO Box), city, state, postal code, and country.

  The address is matched to the closest address or street in the US Postal Service address database. If a match isn’t found, we use the geographical center (average location of addresses) of the 5-digit postal code as a fallback.                                      | ✓ Supported     |
| 9-digit postal code:

  - `postal_code`: 97712-4918
  - `country`: US

  5-digit postal code:

  - `postal_code`: 97712
  - `country`: US | **Country and postal code**

  If you provide a 5-digit or 9-digit postal code, our system only uses the initial 5 digits for tax calculations. The tax is calculated at the geographical center, which reflects the average location of addresses within the 5-digit postal code area. Check that this is [suitable for your business](https://docs.stripe.com/tax/customer-locations.md#us-postal-codes). | ✓ Supported     |
| - `state`: OR
  - `country`: US                                                                                                           | **Country and state**

  We can’t calculate tax for US customers with only an ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [state code](https://en.wikipedia.org/wiki/ISO_3166-2).                                                                                                                                                                                  | ✗ Not supported |
| - `country`: US                                                                                                                           | **Country**

  We can’t calculate tax for US customers with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                                                                                                                       | ✗ Not supported |

#### Canada

| Example addresses                                                                                             | Explanation                                                                                                                                                                                                                                                                                                  | Supported       |
| ------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------- |
| - `line1`: 1 Rocky Road
  - `city`: Vancouver
  - `province`: BC
  - `postal_code`: V1X 1X1
  - `country`: CA | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province. We don’t use the other address fields to calculate tax. | ✓ Supported     |
| 6-digit postal code:

  - `postal_code`: V1X 1X1
  - `country`: CA                                            | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province, which we determine from the postal code.                                                                                                             | ✓ Supported     |
| - `province`: BC
  - `country`: CA                                                                            | **Country and province**

  We calculate tax for Canada customers from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province.                                                                                                                                    | ✓ Supported     |
| - `country`: CA                                                                                               | **Country**

  We can’t calculate tax for Canada customers with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                    | ✗ Not supported |

#### Ukraine

| Example addresses                                                                                            | Explanation                                                                                                                                                                                                                                                                                                     | Supported       |
| ------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: Vyshneva St, 36
  - `city`: Kyiv
  - `province`: UA-32
  - `postal_code`: 01001
  - `country`: UA | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and postal code. We don’t use the other address fields to calculate tax. | ✓ Supported     |
| 5-digit postal code:

  - `postal_code`: 01001
  - `country`: UA                                             | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and postal code.                                                                                                                                                      | ✓ Supported     |
| - `province`: UA-32
  - `country`: UA                                                                        | **Country and province**

  We can’t calculate tax for customers in Ukraine from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [oblast](https://www.iso.org/obp/ui/#iso:code:3166:UA) (province).                                                                    | ✗ Not supported |
| - `country`: UA                                                                                              | **Country**

  We can’t calculate tax for customers in Ukraine with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                   | ✗ Not supported |

#### India

| Example addresses                                                                                                                                                    | Explanation                                                                                                                                                                                                                                                                             | Supported       |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: Rafi Marg, 118
  - `city`: Delhi
  - `province`:  National Capital Territory (NCT) of Delhi (Union Territory)
  - `postal_code`: 110001
  - `country`: IN | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name. | ✓ Supported     |
| 6-digit postal code:

  - `postal_code`: 110001
  - `country`: IN                                                                                                    | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name.                                                                                              | ✓ Supported     |
| - `province`: National Capital Territory (NCT) of Delhi (Union Territory)
  - `country`: IN                                                                          | **Country and province**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name.                                                                                                 | ✓ Supported     |
| - `country`: IN                                                                                                                                                      | **Country**

  We can’t calculate tax for customers in India with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                             | ✗ Not supported |

#### Everywhere else

| Example addresses                                                                            | Explanation                                                                                                                                                                                                                                                                                                                                                                                                                             | Supported   |
| -------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| - `line1`: 1 Grand Canal St
  - `city`: Dublin
  - `postal_code`: D02 H210
  - `country`: IE | **Full address**

  The fields included in a full address differ by country, but typically include the line1 (street address), city, state, postal code, and country.

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes), and the [state code](https://en.wikipedia.org/wiki/ISO_3166-2) or postal code, if provided. The other address fields aren’t used to calculate tax. | ✓ Supported |
| - `postal_code`: 51001
  - `country`: ES                                                     | **Country and postal code**

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes), and use the postal code to determine if your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories).                                                                                                                  | ✓ Supported |
| - `state`: CE
  - `country`: ES                                                              | **Country and state**

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [state code](https://en.wikipedia.org/wiki/ISO_3166-2). We use the state code to determine if your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories).                                                               | ✓ Supported |
| - `country`: ES                                                                              | **Country**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes). If your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories), they pay the country tax rate.                                                                                                                                        | ✓ Supported |

Use one of the above address formats to make sure that we can consistently recognize your connected account’s head office location. The country field must always be a valid [ISO country code](https://en.wikipedia.org/wiki/ISO_3166-1).

> The validation and errors listed here are part of the setup phase. You can still see other errors when trying to [call the API on your Stripe integration](https://docs.stripe.com/tax/set-up.md#integrate).

## See also

- [Use the Registrations API to manage tax registrations](https://docs.stripe.com/tax/registrations-api.md)
- [Use Stripe Tax with Connect](https://docs.stripe.com/tax/connect.md)


# Direct usage

> This is a Direct usage for when tax-integration is direct. View the full page at https://docs.stripe.com/tax/settings-api?tax-integration=direct.

## Check if you’re ready to use Stripe Tax 

Complete this check if you need to assess whether Stripe Tax can be enabled. Use our official libraries for access to the Stripe API. [Retrieve the `tax.settings` object](https://docs.stripe.com/api/tax/settings/retrieve.md):

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:"
```

```cli
stripe tax settings retrieve
```

```ruby
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.retrieve()
```

```python
client = StripeClient("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.retrieve()
```

```php
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->retrieve([]);
```

```java
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsRetrieveParams params = SettingsRetrieveParams.builder().build();

Settings settings = client.v1().tax().settings().retrieve(params);
```

```node
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.retrieve();
```

```go
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxSettingsRetrieveParams{}
result, err := sc.V1TaxSettings.Retrieve(context.TODO(), params)
```

```dotnet
var options = new Stripe.Tax.SettingsGetOptions();
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Get(options);
```

You can also listen to the [tax.settings.updated](https://docs.stripe.com/api/events/types.md#event_types-tax.settings.updated) webhook event, which triggers when you update tax settings through the Stripe Dashboard or when new required tax settings are introduced. See [take webhooks live](https://docs.stripe.com/webhooks.md#register-webhook) to learn how to add a webhook endpoint.

You’re ready to use Stripe Tax if the response `tax.settings` object retrieved by the API or webhook event returns `"active"` for `status`. The `defaults.tax_code` and `defaults.tax_behavior` settings are only required if not provided in the product or price on each API call.

```json
{
  "object": "tax.settings",
  "defaults": {
    "tax_code": null,
    "tax_behavior": null
  },
  "head_office": {
    "address": {
      "country": "DE"
    }
  },
  "livemode": false,"status": "active",
  "status_details": {
    "active": {}
  }
}
```

You aren’t ready to use Stripe Tax if the response `tax.settings` object returns `"pending"` for `status`. The [status_details[pending][missing_fields]](https://docs.stripe.com/api/tax/settings/object.md#tax_settings_object-status_details-pending-missing_fields) has a list of all required missing fields.

```json
{
  "object": "tax.settings",
  "defaults": {
    "tax_code": null,
    "tax_behavior": null
  },
  "head_office": null,
  "livemode": false,"status": "pending",
  "status_details": {
    "pending": {
      "missing_fields": ["head_office"]
    }
  }
}
```

## Configure your settings 

Complete this step when you manage all Stripe Tax configuration through your own interface.

You can modify the settings through an [update settings](https://docs.stripe.com/api/tax/settings/update.md) call. Perform a call providing the head office location, the preset tax code, and the tax behavior.

```curl
curl https://api.stripe.com/v1/tax/settings \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "defaults[tax_code]"=txcd_10000000 \
  -d "defaults[tax_behavior]"=inclusive \
  -d "head_office[address][country]"=DE
```

```cli
stripe tax settings update  \
  -d "defaults[tax_code]"=txcd_10000000 \
  -d "defaults[tax_behavior]"=inclusive \
  -d "head_office[address][country]"=DE
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

settings = client.v1.tax.settings.update({
  defaults: {
    tax_code: 'txcd_10000000',
    tax_behavior: 'inclusive',
  },
  head_office: {address: {country: 'DE'}},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
settings = client.v1.tax.settings.update({
  "defaults": {"tax_code": "txcd_10000000", "tax_behavior": "inclusive"},
  "head_office": {"address": {"country": "DE"}},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$settings = $stripe->tax->settings->update([
  'defaults' => [
    'tax_code' => 'txcd_10000000',
    'tax_behavior' => 'inclusive',
  ],
  'head_office' => ['address' => ['country' => 'DE']],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

SettingsUpdateParams params =
  SettingsUpdateParams.builder()
    .setDefaults(
      SettingsUpdateParams.Defaults.builder()
        .setTaxCode("txcd_10000000")
        .setTaxBehavior(SettingsUpdateParams.Defaults.TaxBehavior.INCLUSIVE)
        .build()
    )
    .setHeadOffice(
      SettingsUpdateParams.HeadOffice.builder()
        .setAddress(
          SettingsUpdateParams.HeadOffice.Address.builder().setCountry("DE").build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Settings settings = client.v1().tax().settings().update(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const settings = await stripe.tax.settings.update({
  defaults: {
    tax_code: 'txcd_10000000',
    tax_behavior: 'inclusive',
  },
  head_office: {
    address: {
      country: 'DE',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.TaxSettingsUpdateParams{
  Defaults: &stripe.TaxSettingsUpdateDefaultsParams{
    TaxCode: stripe.String("txcd_10000000"),
    TaxBehavior: stripe.String(stripe.TaxSettingsDefaultsTaxBehaviorInclusive),
  },
  HeadOffice: &stripe.TaxSettingsUpdateHeadOfficeParams{
    Address: &stripe.AddressParams{Country: stripe.String("DE")},
  },
}
result, err := sc.V1TaxSettings.Update(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new Stripe.Tax.SettingsUpdateOptions
{
    Defaults = new Stripe.Tax.SettingsDefaultsOptions
    {
        TaxCode = "txcd_10000000",
        TaxBehavior = "inclusive",
    },
    HeadOffice = new Stripe.Tax.SettingsHeadOfficeOptions
    {
        Address = new AddressOptions { Country = "DE" },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Tax.Settings;
Stripe.Tax.Settings settings = service.Update(options);
```

The updated `tax.settings` object now has a head office, a preset tax code, and a default tax behavior, which allows you to enable Stripe Tax.

```json
{
  "object": "tax.settings","defaults": {
    "tax_code": "txcd_10000000",
    "tax_behavior": "inclusive"
  },
  "head_office": {
    "address": {
      "country": "DE"
    }
  },
  "livemode": false,
  "status": "active",
  "status_details": {
    "active": {}
  }
}
```

### Validations and errors

The tax codes must refer to [available tax codes](https://docs.stripe.com/tax/tax-codes.md) and the [tax behavior](https://docs.stripe.com/tax/products-prices-tax-codes-tax-behavior.md#tax-behavior) must be set as `inclusive`, `exclusive`, or `inferred_by_currency` (after being set, it can’t be set to null). The `head_office` must include a supported address.

The `head_office[address]` has the fields `line1`, `line2`, `city`, `state`, `postal_code`, and `country`. The tables below describe the supported address formats.

#### United States

| Example addresses                                                                                                                         | Explanation                                                                                                                                                                                                                                                                                                                                                                                                 | Supported       |
| ----------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: 27 Fredrick Ave
  - `city`: Brothers
  - `state`: OR
  - `postal_code`: 97712
  - `country`: US                                | **Full address**

  A full address includes at least a line1 (street address or PO Box), city, state, postal code, and country.

  The address is matched to the closest address or street in the US Postal Service address database. If a match isn’t found, we use the geographical center (average location of addresses) of the 5-digit postal code as a fallback.                                      | ✓ Supported     |
| 9-digit postal code:

  - `postal_code`: 97712-4918
  - `country`: US

  5-digit postal code:

  - `postal_code`: 97712
  - `country`: US | **Country and postal code**

  If you provide a 5-digit or 9-digit postal code, our system only uses the initial 5 digits for tax calculations. The tax is calculated at the geographical center, which reflects the average location of addresses within the 5-digit postal code area. Check that this is [suitable for your business](https://docs.stripe.com/tax/customer-locations.md#us-postal-codes). | ✓ Supported     |
| - `state`: OR
  - `country`: US                                                                                                           | **Country and state**

  We can’t calculate tax for US customers with only an ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [state code](https://en.wikipedia.org/wiki/ISO_3166-2).                                                                                                                                                                                  | ✗ Not supported |
| - `country`: US                                                                                                                           | **Country**

  We can’t calculate tax for US customers with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                                                                                                                       | ✗ Not supported |

#### Canada

| Example addresses                                                                                             | Explanation                                                                                                                                                                                                                                                                                                  | Supported       |
| ------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------- |
| - `line1`: 1 Rocky Road
  - `city`: Vancouver
  - `province`: BC
  - `postal_code`: V1X 1X1
  - `country`: CA | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province. We don’t use the other address fields to calculate tax. | ✓ Supported     |
| 6-digit postal code:

  - `postal_code`: V1X 1X1
  - `country`: CA                                            | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province, which we determine from the postal code.                                                                                                             | ✓ Supported     |
| - `province`: BC
  - `country`: CA                                                                            | **Country and province**

  We calculate tax for Canada customers from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and province.                                                                                                                                    | ✓ Supported     |
| - `country`: CA                                                                                               | **Country**

  We can’t calculate tax for Canada customers with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                    | ✗ Not supported |

#### Ukraine

| Example addresses                                                                                            | Explanation                                                                                                                                                                                                                                                                                                     | Supported       |
| ------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: Vyshneva St, 36
  - `city`: Kyiv
  - `province`: UA-32
  - `postal_code`: 01001
  - `country`: UA | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and postal code. We don’t use the other address fields to calculate tax. | ✓ Supported     |
| 5-digit postal code:

  - `postal_code`: 01001
  - `country`: UA                                             | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and postal code.                                                                                                                                                      | ✓ Supported     |
| - `province`: UA-32
  - `country`: UA                                                                        | **Country and province**

  We can’t calculate tax for customers in Ukraine from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [oblast](https://www.iso.org/obp/ui/#iso:code:3166:UA) (province).                                                                    | ✗ Not supported |
| - `country`: UA                                                                                              | **Country**

  We can’t calculate tax for customers in Ukraine with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                                                   | ✗ Not supported |

#### India

| Example addresses                                                                                                                                                    | Explanation                                                                                                                                                                                                                                                                             | Supported       |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------- |
| - `line1`: Rafi Marg, 118
  - `city`: Delhi
  - `province`:  National Capital Territory (NCT) of Delhi (Union Territory)
  - `postal_code`: 110001
  - `country`: IN | **Full address**

  A full address includes at least a line1 (street address), city, province, postal code, and country.

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name. | ✓ Supported     |
| 6-digit postal code:

  - `postal_code`: 110001
  - `country`: IN                                                                                                    | **Country and postal code**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name.                                                                                              | ✓ Supported     |
| - `province`: National Capital Territory (NCT) of Delhi (Union Territory)
  - `country`: IN                                                                          | **Country and province**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and either the postal code or the province name.                                                                                                 | ✓ Supported     |
| - `country`: IN                                                                                                                                                      | **Country**

  We can’t calculate tax for customers in India with only an [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes).                                                                                                                             | ✗ Not supported |

#### Everywhere else

| Example addresses                                                                            | Explanation                                                                                                                                                                                                                                                                                                                                                                                                                             | Supported   |
| -------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------- |
| - `line1`: 1 Grand Canal St
  - `city`: Dublin
  - `postal_code`: D02 H210
  - `country`: IE | **Full address**

  The fields included in a full address differ by country, but typically include the line1 (street address), city, state, postal code, and country.

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes), and the [state code](https://en.wikipedia.org/wiki/ISO_3166-2) or postal code, if provided. The other address fields aren’t used to calculate tax. | ✓ Supported |
| - `postal_code`: 51001
  - `country`: ES                                                     | **Country and postal code**

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes), and use the postal code to determine if your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories).                                                                                                                  | ✓ Supported |
| - `state`: CE
  - `country`: ES                                                              | **Country and state**

  We calculate tax from the ISO [country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes) and [state code](https://en.wikipedia.org/wiki/ISO_3166-2). We use the state code to determine if your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories).                                                               | ✓ Supported |
| - `country`: ES                                                                              | **Country**

  We calculate tax from the [ISO country code](https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes). If your customer is located in an [excluded territory](https://docs.stripe.com/tax/customer-locations.md#europe-excluded-territories), they pay the country tax rate.                                                                                                                                        | ✓ Supported |

Use one of the above address formats to make sure that we can consistently recognize your head office location. The country field must always be a valid [ISO country code](https://en.wikipedia.org/wiki/ISO_3166-1).

> The validation and errors listed here are part of the setup phase. You can still see other errors when trying to [call the API on your Stripe integration](https://docs.stripe.com/tax/set-up.md#integrate).

## See also

- [Use the Registrations API to manage tax registrations](https://docs.stripe.com/tax/registrations-api.md)

