#server .py

from flask import Flask, request, send_file
import os
app = Flask(__name__)
UPLOAD_FOLDER = 'uploads'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
@app.route('/upload', methods = ["POST"])
def upload_file():
    try:
        file = request.files['file']
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], file.filename)
        file.save(file_path)
        return 'File Uploaded Successfully'
    except Exception as e:
        return str(e), 500
    
@app.route('/download/<filename>', methods = ['GET'])
def download_file(filename):
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    if os.path.exists(file_path):
        return send_file(file_path, as_attachment=True)
    else:
        return 'File Not Found', 404


#then for download:
#http://127.0.0.1:5000/download/down.jpg
    
if __name__=="__main__":
    app.run(debug=True)



#client .py


import requests, os
url = 'http://localhost:5000'
def download_file(filename,download_folder=''):
    download_url=f'{url}/download/{filename}'
    response=requests.get(download_url)
    if response.status_code==200:
        file_path=os.path.join(download_folder,filename)
        with open(file_path,'wb') as f:
            f.write(response.content)
            print(f"file '{filename}' downloaded successfully to '{download_folder}'")
    else:
        print(f"Failed to download file'{filename}':{response.text}")

if __name__=='__main__':
    file_to_download="down.jpg"
    download_folder='resources'
    
download_file(file_to_download,download_folder)   
