TARGETFOLDER=../backend/src/main/resources
WEBFOLDERNAME=web
# step 1: make sure we have someplace to put everything.  We will delete the
#         old folder tree, and then make it from scratch
rm -rf $TARGETFOLDER
mkdir $TARGETFOLDER
mkdir $TARGETFOLDER/$WEBFOLDERNAME

# step 2: update our npm dependencies
# npm update

# step 3: build the project
npm run build

# step 4: Copy the build to the resources
cp -r build/* $TARGETFOLDER/$WEBFOLDERNAME

# step 5: Go to backend and deploy to heroku
cd ../backend
mvn package; mvn heroku:deploy