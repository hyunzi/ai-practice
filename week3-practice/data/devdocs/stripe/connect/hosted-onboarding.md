# Stripe-hosted onboarding

Onboard connected accounts by redirecting them to a Stripe-hosted onboarding flow.

Stripe-hosted onboarding handles the collection of business and identity verification information from connected accounts, requiring minimal effort by the platform. It’s a web form hosted by Stripe that renders dynamically based on the capabilities, country, and business type of each connected account.
![](https://b.stripecdn.com/docs-statics-srv/assets/hosted_onboarding_form.e59ba8300f563e43489953f06127f52c.png)

The hosted onboarding form in the Stripe sample integration, [Furever](https://furever.dev/).

Stripe-hosted onboarding with Accounts v1 supports [networked onboarding](https://docs.stripe.com/connect/networked-onboarding.md), which allows owners of multiple Stripe accounts to share business information between them. When they onboard an account, they can reuse that information from an existing account instead of resubmitting it.

## Customize the onboarding form [Dashboard]

Go to the [Connect settings page](https://dashboard.stripe.com/account/applications/settings) in the Dashboard to customize the visual appearance of the form with your brand’s name, color, and icon. Stripe-hosted onboarding requires this information. Stripe also recommends [collecting bank account information](https://dashboard.stripe.com/settings/connect/payouts/onboarding) from your connected accounts as they’re onboarding.

## Create an account and prefill information [Server-side]

Create a [connected account](https://docs.stripe.com/api/accounts.md) with the default [controller](https://docs.stripe.com/api/accounts/create.md#create_account-controller) properties. See [design an integration](https://docs.stripe.com/connect/design-an-integration.md) to learn more about controller properties. Alternatively, you can create a connected account by specifying an account [type](https://docs.stripe.com/api/accounts/create.md#create_account-type).

If you specify the account’s country or request any capabilities for it, then the account owner can’t change its country. Otherwise, it depends on the account’s Dashboard access:

- **Full Stripe Dashboard:** During onboarding, the account owner can select any acquiring country, the same as when signing up for a normal Stripe account. Stripe automatically requests a set of capabilities for the account based on the selected country.
- **Express Dashboard:** During onboarding, the account owner can select from a list of countries that you configure in your platform Dashboard [Onboarding options](https://dashboard.stripe.com/settings/connect/onboarding-options/countries). You can also configure those options to specify the default capabilities to request for accounts in each country.
- **No Stripe Dashboard**: If Stripe is responsible for collecting requirements, then the onboarding flow lets the account owner select any acquiring country. Otherwise, your custom onboarding flow must set the country and request capabilities.

#### With controller properties

```curl
curl https://api.stripe.com/v1/accounts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d "controller[fees][payer]"=application \
  -d "controller[losses][payments]"=application \
  -d "controller[stripe_dashboard][type]"=express
```

```cli
stripe accounts create  \
  -d "controller[fees][payer]"=application \
  -d "controller[losses][payments]"=application \
  -d "controller[stripe_dashboard][type]"=express
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.create({
  controller: {
    fees: {payer: 'application'},
    losses: {payments: 'application'},
    stripe_dashboard: {type: 'express'},
  },
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.create({
  "controller": {
    "fees": {"payer": "application"},
    "losses": {"payments": "application"},
    "stripe_dashboard": {"type": "express"},
  },
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->create([
  'controller' => [
    'fees' => ['payer' => 'application'],
    'losses' => ['payments' => 'application'],
    'stripe_dashboard' => ['type' => 'express'],
  ],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountCreateParams params =
  AccountCreateParams.builder()
    .setController(
      AccountCreateParams.Controller.builder()
        .setFees(
          AccountCreateParams.Controller.Fees.builder()
            .setPayer(AccountCreateParams.Controller.Fees.Payer.APPLICATION)
            .build()
        )
        .setLosses(
          AccountCreateParams.Controller.Losses.builder()
            .setPayments(AccountCreateParams.Controller.Losses.Payments.APPLICATION)
            .build()
        )
        .setStripeDashboard(
          AccountCreateParams.Controller.StripeDashboard.builder()
            .setType(AccountCreateParams.Controller.StripeDashboard.Type.EXPRESS)
            .build()
        )
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.create({
  controller: {
    fees: {
      payer: 'application',
    },
    losses: {
      payments: 'application',
    },
    stripe_dashboard: {
      type: 'express',
    },
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountCreateParams{
  Controller: &stripe.AccountCreateControllerParams{
    Fees: &stripe.AccountCreateControllerFeesParams{
      Payer: stripe.String(stripe.AccountControllerFeesPayerApplication),
    },
    Losses: &stripe.AccountCreateControllerLossesParams{
      Payments: stripe.String(stripe.AccountControllerLossesPaymentsApplication),
    },
    StripeDashboard: &stripe.AccountCreateControllerStripeDashboardParams{
      Type: stripe.String(stripe.AccountControllerStripeDashboardTypeExpress),
    },
  },
}
result, err := sc.V1Accounts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountCreateOptions
{
    Controller = new AccountControllerOptions
    {
        Fees = new AccountControllerFeesOptions { Payer = "application" },
        Losses = new AccountControllerLossesOptions { Payments = "application" },
        StripeDashboard = new AccountControllerStripeDashboardOptions
        {
            Type = "express",
        },
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Create(options);
```

#### With account type

```curl
curl https://api.stripe.com/v1/accounts \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d type=standard
```

```cli
stripe accounts create  \
  --type=standard
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account = client.v1.accounts.create({type: 'standard'})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account = client.v1.accounts.create({"type": "standard"})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$account = $stripe->accounts->create(['type' => 'standard']);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountCreateParams params =
  AccountCreateParams.builder().setType(AccountCreateParams.Type.STANDARD).build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
Account account = client.v1().accounts().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const account = await stripe.accounts.create({
  type: 'standard',
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountCreateParams{Type: stripe.String(stripe.AccountTypeStandard)}
result, err := sc.V1Accounts.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountCreateOptions { Type = "standard" };
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.Accounts;
Account account = service.Create(options);
```

The response includes the ID, which you use to reference the `Account` throughout your integration.

### Request capabilities

You can request [capabilities](https://docs.stripe.com/connect/account-capabilities.md#creating) when creating an account by setting the desired capabilities’ `requested` property to true. For accounts with access to the Express Dashboard, you can also configure your [Onboarding options](https://dashboard.stripe.com/settings/connect/onboarding-options/countries) to automatically request certain capabilities when creating an account.

Stripe’s onboarding UIs automatically collect the requirements for requested capabilities. To reduce onboarding effort, request only the capabilities you need.

### Prefill information

If you have information about the account holder (like their name, address, or other details), you can simplify onboarding by providing it when you create or update the account. The onboarding interface asks the account holder to confirm the pre-filled information before accepting the [Connect service agreement](https://docs.stripe.com/connect/service-agreement-types.md). The account holder can edit any pre-filled information before they accept the service agreement, even if you provided the information using the Accounts API.

If you onboard an account and your platform provides it with a URL, prefill the account’s [business_profile.url](https://docs.stripe.com/api/accounts/create.md#create_account-business_profile-url). If the business doesn’t have a URL, you can prefill its [business_profile.product_description](https://docs.stripe.com/api/accounts/create.md#create_account-business_profile-product_description) instead.

When testing your integration, use [test data](https://docs.stripe.com/connect/testing.md) to simulate different outcomes including identity verification, business information verification, payout failures, and more.

## Determine the information to collect

As the platform, you must decide if you want to collect the required information from your connected accounts *up front* (Upfront onboarding is a type of onboarding where you collect all required verification information from your users at sign-up) or *incrementally* (Incremental onboarding is a type of onboarding where you gradually collect required verification information from your users. You collect a minimum amount of information at sign-up, and you collect more information as the connected account earns more revenue). Up-front onboarding collects the `eventually_due` requirements for the account, while incremental onboarding only collects the `currently_due` requirements.

| Onboarding type | Advantages                                                                                                                                                                                                               |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Up-front**    | - Normally requires only one request for all information
  - Avoids the possibility of payout and processing issues due to missed deadlines
  - Exposes potential risk early when accounts refuse to provide information |
| **Incremental** | - Accounts can onboard quickly because they don’t have to provide as much information                                                                                                                                    |

To determine whether to use up-front or incremental onboarding, review the [requirements](https://docs.stripe.com/connect/required-verification-information.md) for your connected accounts’ locations and capabilities. While Stripe tries to minimize any impact to connected accounts, requirements might change over time.

For connected accounts where you’re responsible for requirement collection, you can customize the behavior of [future requirements](https://docs.stripe.com/connect/handle-verification-updates.md) using the `collection_options` parameter. To collect the account’s future requirements, set [`collection_options.future_requirements`](https://docs.stripe.com/api/account_links/create.md#create_account_link-collection_options-future_requirements) to `include`.

## Create an Account Link [Server-side]

Create an [Account Link](https://docs.stripe.com/api/account_links/create.md) using the connected account ID and include a [refresh URL](https://docs.stripe.com/connect/hosted-onboarding.md#refresh-url) and a [return URL](https://docs.stripe.com/connect/hosted-onboarding.md#return-url). Stripe redirects the connected account to the refresh URL if the Account Link URL has already been visited, has expired, or is otherwise invalid. Stripe redirects connected accounts to the return URL when they have completed or left the onboarding flow. Additionally, based on the information you need to collect, pass either `currently_due` or `eventually_due` for `collection_options.fields`. This example passes `eventually_due` to use up-front onboarding. For incremental onboarding, set it to `currently_due`.

```curl
curl https://api.stripe.com/v1/account_links \
  -u "<<YOUR_SECRET_KEY>>:" \
  -d account="{{CONNECTEDACCOUNT_ID}}" \
  --data-urlencode refresh_url="https://example.com/refresh" \
  --data-urlencode return_url="https://example.com/return" \
  -d type=account_onboarding \
  -d "collection_options[fields]"=eventually_due
```

```cli
stripe account_links create  \
  --account="{{CONNECTEDACCOUNT_ID}}" \
  --refresh-url="https://example.com/refresh" \
  --return-url="https://example.com/return" \
  --type=account_onboarding \
  -d "collection_options[fields]"=eventually_due
```

```ruby
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = Stripe::StripeClient.new("<<YOUR_SECRET_KEY>>")

account_link = client.v1.account_links.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/refresh',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
  collection_options: {fields: 'eventually_due'},
})
```

```python
# Set your secret key. Remember to switch to your live secret key in production.
# See your keys here: https://dashboard.stripe.com/apikeys
client = StripeClient("<<YOUR_SECRET_KEY>>")

# For SDK versions 12.4.0 or lower, remove '.v1' from the following line.
account_link = client.v1.account_links.create({
  "account": "{{CONNECTEDACCOUNT_ID}}",
  "refresh_url": "https://example.com/refresh",
  "return_url": "https://example.com/return",
  "type": "account_onboarding",
  "collection_options": {"fields": "eventually_due"},
})
```

```php
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
$stripe = new \Stripe\StripeClient('<<YOUR_SECRET_KEY>>');

$accountLink = $stripe->accountLinks->create([
  'account' => '{{CONNECTEDACCOUNT_ID}}',
  'refresh_url' => 'https://example.com/refresh',
  'return_url' => 'https://example.com/return',
  'type' => 'account_onboarding',
  'collection_options' => ['fields' => 'eventually_due'],
]);
```

```java
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
StripeClient client = new StripeClient("<<YOUR_SECRET_KEY>>");

AccountLinkCreateParams params =
  AccountLinkCreateParams.builder()
    .setAccount("{{CONNECTEDACCOUNT_ID}}")
    .setRefreshUrl("https://example.com/refresh")
    .setReturnUrl("https://example.com/return")
    .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
    .setCollectionOptions(
      AccountLinkCreateParams.CollectionOptions.builder()
        .setFields(AccountLinkCreateParams.CollectionOptions.Fields.EVENTUALLY_DUE)
        .build()
    )
    .build();

// For SDK versions 29.4.0 or lower, remove '.v1()' from the following line.
AccountLink accountLink = client.v1().accountLinks().create(params);
```

```node
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
const stripe = require('stripe')('<<YOUR_SECRET_KEY>>');

const accountLink = await stripe.accountLinks.create({
  account: '{{CONNECTEDACCOUNT_ID}}',
  refresh_url: 'https://example.com/refresh',
  return_url: 'https://example.com/return',
  type: 'account_onboarding',
  collection_options: {
    fields: 'eventually_due',
  },
});
```

```go
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
sc := stripe.NewClient("<<YOUR_SECRET_KEY>>")
params := &stripe.AccountLinkCreateParams{
  Account: stripe.String("{{CONNECTEDACCOUNT_ID}}"),
  RefreshURL: stripe.String("https://example.com/refresh"),
  ReturnURL: stripe.String("https://example.com/return"),
  Type: stripe.String("account_onboarding"),
  CollectionOptions: &stripe.AccountLinkCreateCollectionOptionsParams{
    Fields: stripe.String("eventually_due"),
  },
}
result, err := sc.V1AccountLinks.Create(context.TODO(), params)
```

```dotnet
// Set your secret key. Remember to switch to your live secret key in production.
// See your keys here: https://dashboard.stripe.com/apikeys
var options = new AccountLinkCreateOptions
{
    Account = "{{CONNECTEDACCOUNT_ID}}",
    RefreshUrl = "https://example.com/refresh",
    ReturnUrl = "https://example.com/return",
    Type = "account_onboarding",
    CollectionOptions = new AccountLinkCollectionOptionsOptions
    {
        Fields = "eventually_due",
    },
};
var client = new StripeClient("<<YOUR_SECRET_KEY>>");
var service = client.V1.AccountLinks;
AccountLink accountLink = service.Create(options);
```

### Redirect your connected account to the Account Link URL 

Redirect the connected account to the Account Link URL to send them to the onboarding flow. You can only use each temporary Account Link URL once, because it grants access to the account holder’s personal information. Authenticate the account in your application before redirecting them to this URL. [Prefill](https://docs.stripe.com/connect/hosted-onboarding.md#prefill-information) any account information before generating the Account Link because you can’t read or write information for the connected account afterward.

> Don’t email, text, or otherwise send account link URLs outside of your platform application. Instead, provide them to the authenticated account holder within your application.

#### iOS

#### Swift

```swift
import UIKit
import SafariServices

let BackendAPIBaseURL: String = "" // Set to the URL of your backend server

class ConnectOnboardViewController: UIViewController {

    // ...

    override func viewDidLoad() {
        super.viewDidLoad()

        let connectWithStripeButton = UIButton(type: .system)
        connectWithStripeButton.setTitle("Connect with Stripe", for: .normal)
        connectWithStripeButton.addTarget(self, action: #selector(didSelectConnectWithStripe), for: .touchUpInside)
        view.addSubview(connectWithStripeButton)

        // ...
    }

    @objc
    func didSelectConnectWithStripe() {
        if let url = URL(string: BackendAPIBaseURL)?.appendingPathComponent("onboard-user") {
          var request = URLRequest(url: url)
          request.httpMethod = "POST"
          let task = URLSession.shared.dataTask(with: request) { (data, response, error) in
              guard let data = data,
                  let json = try? JSONSerialization.jsonObject(with: data, options: []) as? [String : Any],
                  let accountURLString = json["url"] as? String,
                  let accountURL = URL(string: accountURLString) else {
                      // handle error
              }

              let safariViewController = SFSafariViewController(url: accountURL)
              safariViewController.delegate = self

              DispatchQueue.main.async {
                  self.present(safariViewController, animated: true, completion: nil)
              }
          }
        }
    }

    // ...
}

extension ConnectOnboardViewController: SFSafariViewControllerDelegate {
    func safariViewControllerDidFinish(_ controller: SFSafariViewController) {
        // the user may have closed the SFSafariViewController instance before a redirect
        // occurred. Sync with your backend to confirm the correct state
    }
}

```

#### Objective C

```objc
#import "ConnectOnboardViewController.h"

#import <SafariServices/SafariServices.h>

static NSString * const kBackendAPIBaseURL = @"";  // Set to the URL of your backend server

@interface ConnectOnboardViewController () <SFSafariViewControllerDelegate>
// ...
@end

@implementation ConnectOnboardViewController

// ...

- (void)viewDidLoad {
    [super viewDidLoad];

    UIButton *connectWithStripeButton = [UIButton buttonWithType:UIButtonTypeSystem];
    [connectWithStripeButton setTitle:@"Connect with Stripe" forState:UIControlStateNormal];
    [connectWithStripeButton addTarget:self action:@selector(_didSelectConnectWithStripe) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:connectWithStripeButton];

    // ...
}

- (void)_didSelectConnectWithStripe {
  NSURL *url = [NSURL URLWithString:[kBackendAPIBaseURL stringByAppendingPathComponent:@"onboard-user"]];
  NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
  request.HTTPMethod = @"POST";

  NSURLSessionTask *task = [[NSURLSession sharedSession] dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
      if (data != nil) {
          NSError *jsonError = nil;
          id json = [NSJSONSerialization JSONObjectWithData:data options:0 error:&jsonError];

          if (json != nil && [json isKindOfClass:[NSDictionary class]]) {
              NSDictionary *jsonDictionary = (NSDictionary *)json;
              NSURL *accountURL = [NSURL URLWithString:jsonDictionary[@"url"]];
              if (accountURL != nil) {
                  SFSafariViewController *safariViewController = [[SFSafariViewController alloc] initWithURL:accountURL];
                  safariViewController.delegate = self;

                  dispatch_async(dispatch_get_main_queue(), ^{
                      [self presentViewController:safariViewController animated:YES completion:nil];
                  });
              } else {
                  // handle  error
              }
          } else {
              // handle error
          }
      } else {
          // handle error
      }
  }];
  [task resume];
}

// ...

#pragma mark - SFSafariViewControllerDelegate
- (void)safariViewControllerDidFinish:(SFSafariViewController *)controller {
    // The user may have closed the SFSafariViewController instance before a redirect
    // occurred. Sync with your backend to confirm the correct state
}

@end

```

#### Android

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".activity.ConnectWithStripeActivity">

    <Button
        android:id="@+id/connect_with_stripe"
        android:text="Connect with Stripe"
        android:layout_height="wrap_content"
        android:layout_width="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        style="?attr/materialButtonOutlinedStyle"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Kotlin

```kotlin
class ConnectWithStripeActivity : AppCompatActivity() {

    private val viewBinding: ActivityConnectWithStripeViewBinding by lazy {
        ActivityConnectWithStripeViewBinding.inflate(layoutInflater)
    }
    private val httpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.connectWithStripe.setOnClickListener {
            val weakActivity = WeakReference<Activity>(this)
            val request = Request.Builder()
                .url(BACKEND_URL + "onboard-user")
                .post("".toRequestBody())
                .build()
            httpClient.newCall(request)
                .enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        // Request failed
                    }
                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            // Request failed
                        } else {
                            val responseData = response.body?.string()
                            val responseJson =
                                responseData?.let { JSONObject(it) } ?: JSONObject()
                            val url = responseJson.getString("url")

                            weakActivity.get()?.let {
                                val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
                                val customTabsIntent = builder.build()
                                customTabsIntent.launchUrl(it, Uri.parse(url))
                            }
                        }
                    }
                })
        }
    }

    internal companion object {
        internal const val BACKEND_URL = "https://example-backend-url.com/"
    }
}
```

#### Java

```java
public class ConnectWithStripeActivity extends AppCompatActivity {
    private static final String BACKEND_URL = "https://example-backend-url.com/";
    private OkHttpClient httpClient = new OkHttpClient();
    private ActivityConnectWithStripeViewBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityConnectWithStripeViewBinding.inflate(getLayoutInflater());

        viewBinding.connectWithStripe.setOnClickListener(view -> {
            WeakReference<Activity> weakActivity = new WeakReference<>(this);
            Request request = new Request.Builder()
                    .url(BACKEND_URL + "onboard-user")
                    .post(RequestBody.create("", MediaType.get("application/json; charset=utf-8")))
                    .build();
            httpClient.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            // Request failed
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final Activity activity = weakActivity.get();
                            if (activity == null) {
                                return;
                            }
                            if (!response.isSuccessful() || response.body() == null) {
                                // Request failed
                            } else {
                                String body = response.body().string();
                                try {
                                    JSONObject responseJson = new JSONObject(body);
                                    String url = responseJson.getString("url");
                                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                    CustomTabsIntent customTabsIntent = builder.build();
                                    customTabsIntent.launchUrl(view.getContext(), Uri.parse(url));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        });
    }
}
```

## Identify and address requirement updates [Server-side]

Set up your integration to [listen for changes](https://docs.stripe.com/connect/handling-api-verification.md#verification-process) to account requirements. You can test handling new requirements (and how they might disable charges and payouts) with the [test trigger cards](https://docs.stripe.com/connect/testing.md#trigger-cards).

Send a connected account back through onboarding when it has any `currently_due` or `eventually_due` requirements. You don’t need to identify the specific requirements, because the onboarding interface knows what information it needs to collect. For example, if a typo is preventing verification of the account owner’s identity, onboarding prompts them to upload an identity document.

Stripe notifies you about any [upcoming requirements updates](https://support.stripe.com/user/questions/onboarding-requirements-updates) that affect your connected accounts. You can proactively collect this information by reviewing the [future requirements](https://docs.stripe.com/api/accounts/object.md#account_object-future_requirements) for your accounts.

For connected accounts where [controller.requirement_collection](https://docs.stripe.com/api/accounts/object.md#account_object-controller-requirement_collection) is `stripe`, stop receiving updates for identity information after creating an [Account Link](https://docs.stripe.com/api/account_links.md) or [Account Session](https://docs.stripe.com/api/account_sessions.md).

Accounts store identity information in the `company` and `individual` hashes.

### Handle verification errors 

Listen to the [account.updated](https://docs.stripe.com/api/events/types.md#event_types-account.updated) event. If the account contains any `currently_due` fields when the `current_deadline` arrives, the corresponding functionality is disabled and those fields are added to `past_due`.

Let your accounts remediate their verification requirements by directing them to the Stripe-hosted onboarding form.
 (See full diagram at https://docs.stripe.com/connect/hosted-onboarding)
## Handle the connected account returning to your platform [Server-side]

The Account Link requires a `refresh_url` and `return_url` to handle all cases in which the connected account is redirected back to your platform. It’s important to implement these correctly to provide the best onboarding flow for your connected accounts.

> You can use HTTP for your `refresh_url` and `return_url` while you’re in a testing environment (for example, to test locally), but live mode only accepts HTTPS. Be sure you’ve swapped any testing URLs for HTTPS URLs before going live.

### Refresh URL 

Your connected account is redirected to the `refresh_url` when:

- The link is expired (a few minutes went by since the link was created).
- The link was already visited (the connected account refreshed the page or clicked the **back** or **forward** button).
- The link was shared in a third-party application such as a messaging client that attempts to access the URL to preview it. Many clients automatically visit links, which causes an Account Link to expire.

The `refresh_url` should call a method on your server to create a new Account Link with the same parameters and redirect the connected account to the new Account Link URL.

### Return URL 

Stripe redirects the connected account back to this URL when they complete the onboarding flow or click **Save for later** at any point in the flow. It doesn’t mean that all information has been collected, or that there are no outstanding requirements on the account. It only means the flow was entered and exited properly.

No state is passed with this URL. After a connected account is redirected to the `return_url`, determine if the account has completed onboarding. [Retrieve the account](https://docs.stripe.com/api/accounts/retrieve.md) and check the [requirements](https://docs.stripe.com/api/accounts/object.md#account_object-requirements) hash for outstanding requirements. Alternatively, listen to the `account.updated` event sent to your webhook endpoint and cache the state of the account in your application. If the account hasn’t completed onboarding, provide prompts in your application to allow them to continue onboarding later.

## Handle connected account-initiated updates [Server-side]

Stripe-hosted onboarding also supports connected account-initiated updates to the information they’ve already provided. Listen to the `account.updated` event sent to your webhook endpoint to be notified when the account completes requirements and updates their information.

When you create an Account Link, you can set the `type` to either `account_onboarding` or `account_update`.

> #### Account Link type restriction
> 
> You can create Account Links of type `account_update` only for connected accounts where your platform is responsible for collecting requirements, including Custom accounts. You can’t create them for accounts that have access to a Stripe-hosted Dashboard. If you use [Connect embedded components](https://docs.stripe.com/connect/get-started-connect-embedded-components.md), you can include components that allow your connected accounts to update their own information. For an account without Stripe-hosted Dashboard access where Stripe is liable for negative balances, you must use embedded components.

### Account Links for account_onboarding 

Account Links of this type provide a form for inputting outstanding requirements. Use it when you’re onboarding a new connected account, or when an existing user has new requirements (such as when a connected account had already provided enough information, but you requested a new capability that needs additional info). Send them to this type of Account Link to just collect the new information you need.

### Account Links for account_update 

Account Links of this type are enabled for accounts where your platform is responsible for requirement collection. `account_update` links display the attributes that are already populated on the account object and allow the connected account to edit previously provided information. Provide an option in your application (for example, “edit my profile” or “update my verification information”) for connected accounts to make updates themselves.

## Browser support

Stripe-hosted onboarding is only supported in web browsers. You can’t use it in embedded web views inside mobile or desktop applications.
