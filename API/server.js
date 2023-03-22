const fs = require("fs");
const bodyParser = require("body-parser");
const jsonServer = require("json-server");
const jwt = require("jsonwebtoken");
const { emitWarning } = require("process");

const server = jsonServer.create();

const router = jsonServer.router("./db.json");

const db = JSON.parse(fs.readFileSync("./db.json", "UTF-8"));

const middlewares = jsonServer.defaults();
const PORT = process.env.PORT || 3000;

server.use(middlewares);

server.use(jsonServer.defaults());
server.use(bodyParser.urlencoded({ extended: true }));
server.use(bodyParser.json());

const SECRET_KEY = "123456789";
const expiresIn = "1h";

function createToken(payload) {
  return jwt.sign(payload, SECRET_KEY, { expiresIn });
}

function verifyToken(token) {
  return jwt.verify(token, SECRET_KEY, (err, decode) =>
    decode !== undefined ? decode : err
  );
}

function isAuthenticated({ email, password }) {
  return (
    db.account.findIndex(
      (user) => user.email === email && user.password === password
    ) !== -1
  );
}
//Random info
server.get("/info", (req, res) => {
  let phone = "03";
  for (let i = 0; i < 8; i++) {
    phone += Math.floor(Math.random() * 10).toString();
  }

  const name = "Lư Hữu Đức" + db.account.length;
  const email = "lhd.vie@gmail.com" + db.account.length;
  const password = "522003";
  const address = "8/5 Vườn lài" + db.account.length;

  const info = {
    name,
    phone,
    email,
    password,
    address,
  };

  res.status(200).json({
    status: 200,
    data: info,
  });
});

//Register
server.post("/register", (req, res) => {
  const { name, phone, email, password } = req.body;

  exist_email = db.account.findIndex((x) => x.email === email);
  exist_phone = db.account.findIndex((x) => x.phone === phone);

  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
  const phoneNumberRegex = /^(0|\+84)(3\d{8}|5\d{8}|7\d{8}|8\d{8}|9\d{8})$/;

  const checkEmail = emailRegex.test(email);
  const checkPhone = phoneNumberRegex.test(phone);
  const checkPassword = password.length > 5;

  if (name == "" || phone == "" || email == "" || password == "") {
    res.status(401).json({
      status: "401",
      message: "Please enter your registration information",
    });
  } else {
    if (
      exist_email == -1 &&
      exist_phone == -1 &&
      checkEmail &&
      checkPhone &&
      checkPassword
    ) {
      const new_user = {
        id: db.account.length + 1,
        name,
        phone,
        email,
        password,
        address: ""
      };

      db.account.push(new_user);
      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });
      res.status(201).json({
        status: 201,
        message: "Success",
        data: new_user,
      });
    } else {
      let message = [];
      if (exist_email !== -1) {
        message.push("Email already exists");
      }
      if (exist_phone !== -1) {
        message.push("Phone number already exists");
      }
      if (!checkEmail) {
        message.push("Misformatted emails");
      }
      if (!checkPhone) {
        message.push("The phone number is not properly formatted");
      }
      if (!checkPassword) {
        message.push("Passwords from 6 characters in length");
      }

      res.status(401).json({
        status: 401,
        message: message,
      });
    }
  }
});

//login
server.post("/login", (req, res) => {
  const { email, password } = req.body;

  const emailRegex = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;

  const checkEmail = emailRegex.test(email);
  const checkPassword = password.length > 5;

  if (email == "" || password == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (checkEmail && checkPassword) {
      if (isAuthenticated({ email, password }) === false) {
        const status = 401;
        const message = "Incorrect email or password";
        res.status(status).json({ status, message });
        return;
      }
      const access_token = createToken({ email, password });
      res.status(201).json({
        status: 201,
        message: "Success",
        data: {
          access_token,
        },
      });
    } else {
      let message = [];
      if (!checkEmail) {
        message.push("Misformatted emails");
      }
      if (!checkPassword) {
        message.push("Passwords over 6 characters in length");
      }

      res.status(401).json({
        status: 401,
        message: message,
      });
    }
  }
});

server.use("/auth", (req, res, next) => {
  if (
    req.headers.authorization == undefined ||
    req.headers.authorization.split(" ")[0] !== "Bearer"
  ) {
    const status = 401;
    const message = "Bad authorization header";
    res.status(status).json({ status, message });
    return;
  }
  try {
    let verifyTokenResult;
    verifyTokenResult = verifyToken(req.headers.authorization.split(" ")[1]);

    if (verifyTokenResult instanceof Error) {
      const status = 401;
      const message = "Error: access_token is not valid";
      res.status(status).json({ status, message });
      return;
    }
    next();
  } catch (err) {
    const status = 401;
    const message = "Token đã hết hạn";
    res.status(status).json({ status, message });
  }
});

//view all account
server.get("/auth/accounts", (req, res) => {
  res.status(200).json({
    status: 200,
    length: db.account.length,
    data: {
      account: db.account,
    },
  });
});

//Xem thông tin account theo email
server.get("/auth/account/:email", (req, res) => {
  const email = req.params.email;

  const exist_email = db.account.findIndex((account) => account.email == email);
  const result = db.account.filter((account) => account.email == email);
  if (exist_email !== -1) {
    const status = 200;
    return res.status(status).json({ status, result });
  } else {
    return res.status(404).json({
      status: 404,
      message: "Email is not found!!",
    });
  }
});

//update account
server.patch("/auth/account/:email", (req, res) => {
  const email_params = req.params.email;
  const name = req.body.name;
  const phone = req.body.phone;
  const password = req.body.password;
  const address = req.body.address;
  const exist_account = db.account.findIndex(
    (account) => account.email == email_params
  );

  const checkPassword = password.length > 5;
  const phoneNumberRegex = /^(0|\+84)(3\d{8}|5\d{8}|7\d{8}|8\d{8}|9\d{8})$/;
  const checkPhone = phoneNumberRegex.test(phone);

  if (name == "" || phone == "" || password == "" || address == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (exist_account !== -1 && checkPassword && checkPhone) {
      db.account[exist_account].name = name;
      db.account[exist_account].phone = phone;
      db.account[exist_account].papassword = password;
      db.account[exist_account].address = address;

      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      res.status(200).json({
        status: 200,
        message: "Success",
        data: {
          account: db.account[exist_account],
        },
      });
    } else {
      let message = [];
      if (exist_account == -1) {
        message.push("Account doesn't exist");
      }
      if (!checkPassword) {
        message.push("The phone number is not properly formatted");
      }
      if (!checkPhone) {
        message.push("The phone number is not properly formatted");
      }
      res.status(401).json({
        status: 401,
        message: message,
      });
    }
  }

  if (exist_account !== -1) {
    db.account[exist_account].name = name;
    db.account[exist_account].phone = phone;
    db.account[exist_account].papassword = password;
    db.account[exist_account].address = address;

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: {
        account: db.account[exist_account],
      },
    });
  } else {
    res.status(404).json({
      status: 404,
      message: "Account not found!!",
    });
  }
});

//delete account by email
server.delete("/auth/account/:email", (req, res) => {
  const email = req.params.email;

  const exist_email = db.account.findIndex((user) => user.email == email);
  if (exist_email !== -1) {
    db.account.splice(exist_email, 1);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: db.account,
    });
  } else {
    res.status(404).json({
      status: 404,
      message: "Email is not found!!",
    });
  }
});

//Xem danh sách vị trí
server.get("/locations", (req, res) => {
  return res.status(200).json({
    status: 200,
    length: db.location.length,
    data: {
      location: db.location,
    },
  });
});

//xem vị trí theo code
server.get("/location/:code", (req, res) => {
  const code = req.params.code;
  const exist_location = db.location.findIndex((item) => item.code == code);
  if (exist_location !== -1) {
    res.status(200).json({
      status: 200,
      massage: "Success",
      data: {
        location: db.location[exist_location],
      },
    });
  } else {
    return res.status(404).json({
      status: 404,
      message: "Location not found!",
    });
  }
});

//Thêm vị ví
server.post("/auth/add_location", (req, res) => {
  const { code, content } = req.body;

  const exist_location = db.location.findIndex((item) => item.code === code);

  if (code == "" || content == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (exist_location == -1) {
      const location = {
        id: db.location.length + 1,
        code,
        content,
      };

      db.location.push(location);
      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      return res.status(201).json({
        status: 201,
        message: "Success",
        data: location,
      });
    } else {
      return res.status(401).json({
        status: 401,
        message: "Location already exists",
      });
    }
  }
});

//cập nhật vị trí
server.patch("/auth/location/:code", (req, res) => {
  const code_params = req.params.code;
  const { code, content } = req.body;

  const index = db.location.findIndex((item) => item.code == code_params);

  if (code == "" || content == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (index !== -1) {
      db.location[index].code = code;
      db.location[index].content = content;

      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      res.status(200).json({
        status: 200,
        message: "Success",
        data: {
          location: db.location[index],
        },
      });
    } else {
      return res.status(404).json({
        status: 404,
        message: "Location not found!",
      });
    }
  }
});

//xóa vị trí
server.delete("/auth/location/:code", (req, res) => {
  const code_params = req.params.code;
  const index = db.location.findIndex((item) => item.code == code_params);
  if (index !== -1) {
    db.location.splice(index, 1);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: db.location,
    });
  } else {
    return res.status(404).json({
      status: 404,
      message: "Location not found!",
    });
  }
});

//lấy dữ liệu store
server.get("/stores", (req, res) => {
  res.status(200).json({
    status: 200,
    data: {
      store: db.store,
    },
  });
});

// Lấy store theo vị trí
server.get("/store/location/:location", (req, res) => {
  const location = req.params.location;
  const list = db.store.filter((item) => item.location == location);

  if (list.length > 0) {
    res.status(200).json({
      status: 200,
      data: {
        store: list,
      },
    });
  } else {
    res.status(404).json({
      status: 404,
      massage: "No data found",
    });
  }
});

//Lấy store theo code
server.get("/store/code/:code", (req, res) => {
  const code = req.params.code;
  const index = db.store.findIndex((item) => item.code == code);

  if (index !== -1) {
    res.status(200).json({
      status: 200,
      data: db.store[index],
    });
  } else {
    res.status(404).json({
      status: 404,
      massage: "No data found",
    });
  }
});

//lấy dữ liệu của store dựa trên vị trí và mã cửa cửa hàng
server.get("/store/:location/:code", (req, res) => {
  const code = req.params.code;
  const location = req.params.location;

  const list = db.store.filter((item) => item.location == location);
  const index = list.findIndex((item) => item.code == code);

  if (index !== -1) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: list[index],
    });
  } else {
    res.status(404).json({
      status: 404,
      message: "No data found",
    });
  }
});

//Thêm store
server.post("/auth/store/add_store", (req, res) => {
  const { location, name, code, address, phone } = req.body;

  const exist_location = db.location.findIndex((item) => item.code == location);
  const exist_store = db.store.findIndex((item) => item.code == code);

  const phoneNumberRegex = /^(0|\+84)(3\d{8}|5\d{8}|7\d{8}|8\d{8}|9\d{8})$/;
  const checkPhone = phoneNumberRegex.test(phone);
  const store = {
    id: db.store.length + 1,
    name,
    code,
    address,
    phone,
  };

  if (
    location == "" ||
    name == "" ||
    code == "" ||
    address == "" ||
    phone == ""
  ) {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (exist_location !== -1 && exist_store == -1 && checkPhone) {
      db.store.push(store);
      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });
      return res.status(201).json({
        status: 201,
        message: "Success",
        data: store,
      });
    } else {
      let message = [];
      if (exist_location == -1) {
        message.push("Location does not exist");
      }
      if (exist_store !== -1) {
        message.push("The store already exists in the system");
      }
      if (!checkPhone) {
        message.push("The phone number is not properly formatted");
      }
      res.status(401).json({
        status: 401,
        message: message,
      });
    }
  }
});

//cập nhật store
server.patch("/auth/store/:code", (req, res) => {
  const code_params = req.params.code;
  const { location, name, code, address, phone } = req.body;

  const exist_store = db.store.findIndex((item) => item.code == code_params);
  const exist_location = db.location.findIndex((item) => item.code == location);

  const phoneNumberRegex = /^(0|\+84)(3\d{8}|5\d{8}|7\d{8}|8\d{8}|9\d{8})$/;
  const checkPhone = phoneNumberRegex.test(phone);

  if (
    location == "" ||
    name == "" ||
    code == "" ||
    address == "" ||
    phone == ""
  ) {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (exist_store !== -1 && exist_location !== -1 && checkPhone) {
      db.store[exist_store].location = location;
      db.store[exist_store].name = name;
      db.store[exist_store].code = code;
      db.store[exist_store].address = address;
      db.store[exist_store].phone = phone;

      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      res.status(200).json({
        status: 200,
        message: "Success",
        data: db.store[exist_store],
      });
    } else {
      let message = [];
      if (exist_location == -1) {
        message.push("Location does not exist");
      }
      if (exist_store == -1) {
        message.push("The store does not exist");
      }
      if (!checkPhone) {
        message.push("The phone number is not properly formatted");
      }
      res.status(401).json({
        status: 401,
        message: message,
      });
    }
  }
});

//xóa store
server.delete("/auth/store/:code", (req, res) => {
  const code_params = req.params.code;
  const index = db.store.findIndex((item) => item.code == code_params);
  if (index !== -1) {
    db.store.splice(index, 1);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: db.store,
    });
  } else {
    return res.status(404).json({
      status: 404,
      message: "Store not found!",
    });
  }
});

//Lấy dữ liệu của product
server.get("/products", (req, res) => {
  let list = db.product;
  for (let i = 0; i < list.length; i++) {
    if (!list[i].updated) {
      list[i].updated = true;
      for (let j = 0; j < list[i].version.length; j++) {
        for (let k = 0; k < list[i].color.length; k++) {
          if (
            list[i].color[k].quantity > 0 &&
            list[i].color[k].storage == list[i].version[j].storage
          ) {
            list[i].version[j].quantity += list[i].color[k].quantity;
          }
          if (list[i].color[k].quantity <= 0) {
            list[i].color[k].quantity = "Out of stock";
          }
        }
        list[i].inventory += list[i].version[j].quantity;
      }
    }
  }

  fs.writeFileSync("./db.json", JSON.stringify(db), () => {
    if (err) return console.log(err);
    console.log("writing to " + fileName);
  });

  res.status(200).json({
    status: 200,
    message: "Success",
    data: list,
  });
});

//lấy dữ liệu của product thông qua location
server.get("/product/location/:location", (req, res) => {
  const location = req.params.location;
  const store = db.store.filter((item) => item.location == location);
  let list = [];

  for (let i = 0; i < store.length; i++) {
    for (let j = 0; j < db.product.length; j++) {
      for (let k = 0; k < db.product[j].branch.length; k++) {
        if (
          db.product[j].branch[k] == store[i].code &&
          list.findIndex((item) => item.id == db.product[j].id) == -1
        ) {
          list.push(db.product[j]);
        }
      }
    }
  }

  if (store.length > 0 && list.length > 0) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: list,
    });
  } else {
    let message = [];
    if (store.length.length == 0) {
      message.push("Can't find stores by this location");
    }
    if (list.length == 0) {
      message.push("No products found in this location");
    }

    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//lấy dữ liệu của product thông qua location và id
server.get("/product/location/:location/:id", (req, res) => {
  const location = req.params.location;
  const id = req.params.id;
  const store = db.store.filter((item) => item.location == location);
  let list = [];

  for (let i = 0; i < store.length; i++) {
    for (let j = 0; j < db.product.length; j++) {
      for (let k = 0; k < db.product[j].branch.length; k++) {
        if (
          db.product[j].branch[k] == store[i].code &&
          list.findIndex((item) => item.id == db.product[j].id) == -1
        ) {
          list.push(db.product[j]);
        }
      }
    }
  }

  const exits_product = list.findIndex((item) => item.id == id);

  if (store.length > 0 && exits_product !== -1 && list.length > 0) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: list[exits_product],
    });
  } else {
    let message = [];
    if (store.length.length == 0) {
      message.push("Can't find stores by this location");
    }
    if (list.length == 0) {
      message.push("No products found in this location");
    }
    if (exits_product == -1) {
      message.push("No products found");
    }

    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//Lấy dữ liệu product thông qua store
server.get("/product/store/:store", (req, res) => {
  const store = req.params.store;
  const index_store = db.store.findIndex((item) => item.code == store);
  const list = [];

  for (let i = 0; i < db.product.length; i++) {
    for (let j = 0; j < db.product[i].branch.length; j++) {
      if (db.product[i].branch[j] == store) {
        list.push(db.product[i]);
      }
    }
  }

  if (list.length > 0 && index_store !== -1) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: list,
    });
  } else {
    let message = [];
    if (list.length == 0) {
      message.push("No products found in this store");
    }
    if (index_store == -1) {
      message.push("Store not found");
    }
    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//Lấy dữ liệu product thông qua store và id
server.get("/product/store/:store/:id", (req, res) => {
  const store = req.params.store;
  const id = req.params.id;
  const index_store = db.store.findIndex((item) => item.code == store);
  const list = [];

  for (let i = 0; i < db.product.length; i++) {
    for (let j = 0; j < db.product[i].branch.length; j++) {
      if (db.product[i].branch[j] == store) {
        list.push(db.product[i]);
      }
    }
  }

  const index = list.findIndex((item) => item.id == id);

  if (list.length > 0 && index_store !== -1 && index !== -1) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: list[index],
    });
  } else {
    let message = [];
    if (list.length == 0) {
      message.push("No products found in this store");
    }
    if (index_store == -1) {
      message.push("Store not found");
    }
    if (index == -1) {
      message.push("Product not found");
    }
    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//lấy dữ liệu product thông qua id
server.get("/product/:id", (req, res) => {
  const id = req.params.id;

  const index = db.product.findIndex((item) => item.id == id);

  if (index !== -1) {
    res.status(200).json({
      status: 200,
      message: "Success",
      data: db.product[index],
    });
  } else {
    let message = [];
    if (index == -1) {
      message.push("No products found");
    }

    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//thêm sản phẩm
server.post("/auth/product/add_product", (req, res) => {
  const {
    name,
    category,
    brand,
    branch,
    version,
    image,
    color,
    option,
    sale,
    warranty,
    promotion,
    status,
    gift,
    specifications,
    desc,
  } = req.body;

  if (name == "" || category == "" || brand == "" || desc == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    const product = {
      id: db.product.length + 1,
      name,
      price: 0,
      category,
      brand,
      branch,
      version,
      image,
      color,
      option,
      sale,
      warranty,
      promotion,
      status,
      gift,
      specifications,
      inventory: 0,
      sell_number: 0,
      updated: false,
      comment: [],
      desc,
    };

    db.product.push(product);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(201).json({
      status: 201,
      message: "Success",
      data: product,
    });
  }
});

//cập nhật sản phẩm
server.patch("/auth/product/:id", (req, res) => {
  const id = req.params.id;
  const {
    name,
    category,
    brand,
    branch,
    version,
    image,
    color,
    option,
    sale,
    warranty,
    promotion,
    status,
    gift,
    specifications,
    desc,
  } = req.body;

  const index = db.product.findIndex((item) => item.id == id);

  if (name == "" || category == "" || brand == "" || desc == "") {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (index !== -1) {
      db.product[index].name = name;
      db.product[index].category = category;
      db.product[index].brand = brand;
      db.product[index].branch = branch;
      db.product[index].version = version;
      db.product[index].image = image;
      db.product[index].color = color;
      db.product[index].option = option;
      db.product[index].warranty = warranty;
      db.product[index].sale = sale;
      db.product[index].promotion = promotion;
      db.product[index].status = status;
      db.product[index].gift = gift;
      db.product[index].specifications = specifications;
      db.product[index].desc = desc;

      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      res.status(201).json({
        status: 201,
        message: "Success",
        data: db.product[index],
      });
    } else {
      let message = [];
      if (index == -1) {
        message.push("Product not found!!");
      }

      res.status(404).json({
        status: 404,
        message: message,
      });
    }
  }
});

//xóa sản phẩm
server.delete("/auth/product/:id", (req, res) => {
  const id = req.params.id;
  const index = db.product.findIndex((item) => item.id == id);

  if (index !== -1) {
    db.product.splice(index, 1);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    return res.status(200).json({
      status: 200,
      message: "Success",
      data: db.product,
    });
  } else {
    let message = [];
    if (index == -1) {
      message.push("Product not found!!");
    }
    res.status(404).json({
      status: 404,
      message: message,
    });
  }
});

//mua hàng
server.post("/auth/purchase", (req, res) => {
  const { location, store, id_account, id_product, quantity, version, color } =
    req.body;

  const exist_location = db.location.findIndex((item) => item.code == location);
  const list_store = db.store.filter((item) => item.location == location);
  const exist_store = list_store.findIndex((item) => item.code == store);
  const exist_account = db.account.findIndex((item) => item.id == id_account);
  const exist_product = db.product.findIndex((item) => item.id == id_product);
  const exist_v = db.product[exist_product].version.findIndex(
    (item) => item.storage == version
  );
  const list_color = db.product[exist_product].color.filter(
    (item) => item.storage == version
  );
  const exist_c = list_color.findIndex((item) => item.color == color);

  if (
    location == "" ||
    store == "" ||
    id_account == "" ||
    id_product == "" ||
    quantity == "" ||
    version == "" ||
    color == ""
  ) {
    res.status(401).json({
      status: 401,
      message: "Please enter full information",
    });
  } else {
    if (
      exist_location !== -1 &&
      exist_store !== -1 &&
      exist_account !== -1 &&
      exist_v !== -1 &&
      exist_c !== -1 &&
      db.account[exist_account].address !== "" &&
      db.product[exist_product].color[exist_c].quantity > 0
    ) {
      let price = db.product[exist_product].color[exist_c].price;
      var today = new Date();
      var date =
        today.getDate() +
        "-" +
        (today.getMonth() + 1) +
        "-" +
        today.getFullYear();

      const bill = {
        id: db.bill.length + 1,
        account: db.account[exist_account],
        product: {
          id: db.product[exist_product].id,
          name: db.product[exist_product].name,
          version: version,
          quantity: quantity,
          price: price,
          total: quantity * price,
        },
        date: date,
      };

      db.bill.push(bill);

      fs.writeFileSync("./db.json", JSON.stringify(db), () => {
        if (err) return console.log(err);
        console.log("writing to " + fileName);
      });

      res.status(201).json({
        status: 201,
        data: bill,
      });
    } else {
      let message = [];
      if (exist_location == -1) {
        message.push("Location not found!!");
      }
      if (exist_store == -1) {
        message.push("No stores found by location provided");
      }
      if (exist_account == -1) {
        message.push("Account not found!!");
      }
      else{
        if (db.account[exist_account].address == "") {
          message.push("Please update your address before making a purchase");
        }
      }
      if (exist_v == -1) {
        message.push("This version was not found in the product");
      }
      if (exist_c == -1) {
        message.push(
          "This color is not found in the currently selected version"
        );
      }
      if(db.product[exist_product].color[exist_c].quantity == 0 ||
        db.product[exist_product].color[exist_c].quantity == "Out of stock"){
          message.push(
            "The product is out of stock"
          );
        }

      res.status(404).json({
        status: 404,
        message: message,
      });
    }
  }
});

//lấy dữ liệu bill
server.get("/auth/bills", (req, res) => {
  res.status(200).json({
    status: 200,
    message: "Success",
    data: db.bill,
  });
});

//delete bill
server.delete("/auth/bill/:id", (req, res) => {
  const id = req.params.id;
  const index = db.bill.findIndex((item) => item.id == id);

  if (index !== -1) {
    db.bill.splice(index, 1);

    fs.writeFileSync("./db.json", JSON.stringify(db), () => {
      if (err) return console.log(err);
      console.log("writing to " + fileName);
    });

    res.status(200).json({
      status: 200,
      message: "Success",
      data: db.bill,
    });
  } else {
    res.status(404).json({
      status: 404,
      message: "Bill not found!!",
    });
  }
});

server.use(router);

server.listen(PORT, () => {
  console.log("Run Auth API Server");
});
