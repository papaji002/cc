#Client
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class JavaClient {
    public static void main(String[] args) throws Exception {
        String url = "http://localhost:5000/convert";
        System.out.println("Enter The Amount you want to convert : ");
        Scanner sc = new Scanner(System.in);
        double amountInRs = sc.nextDouble();
        String jsonInpuString = "{\"amt_in_rs\":" + amountInRs + "}";
        sc.close();
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(jsonInpuString.getBytes());
        os.flush();
        os.close();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        System.out.println("Response Code : " + responseCode);
        System.out.println("Response : " + response.toString());
    }
}


#server

from flask import Flask, request, jsonify
app = Flask(__name__)
@app.route('/convert', methods= ['POST'])
def convert_currency():
    try:
        data = request.json
        amt_in_rs = data['amt_in_rs']
        conversion_rate = 0.012
        amt_in_usd = amt_in_rs * conversion_rate
        return jsonify({'amount_in_usd': format(amt_in_usd, '.4f')})
    except Exception as e:
        return jsonify ({'error ': str(e)}), 500
    
if __name__ == "__main__":
    app.run(debug=True)
